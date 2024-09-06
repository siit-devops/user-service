package com.devops.user_service.service.impl;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.EditUserRequest;
import com.devops.user_service.dto.UserDto;
import com.devops.user_service.dto.UsersFromReservationDetails;
import com.devops.user_service.exception.BadRequestException;
import com.devops.user_service.exception.NotFoundException;
import com.devops.user_service.kafka.AccomodationRatingMessage;
import com.devops.user_service.kafka.HostRatingMessage;
import com.devops.user_service.kafka.NotificationMessage;
import com.devops.user_service.kafka.ReservationStatusUpdateMessage;
import com.devops.user_service.kafka.enumerations.NotificationType;
import com.devops.user_service.kafka.enumerations.ReservationStatus;
import com.devops.user_service.mappers.MapStructMapper;
import com.devops.user_service.model.User;
import com.devops.user_service.repository.UserRepository;
import com.devops.user_service.service.KeycloakAdminClientService;
import com.devops.user_service.service.UserService;
import com.devops.user_service.service.feignClients.AccommodationClient;
import com.devops.user_service.service.feignClients.ReservationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminClientService keycloakService;
    private final MapStructMapper mapper;

    private final ReservationClient reservationClient;
    private final AccommodationClient accommodationClient;

    @Override
    @Transactional
    public void updateUser(UUID id, EditUserRequest editUserRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User doesn't exist")
        );

        if (editUserRequest.getId().compareTo(user.getId()) != 0) {
            throw new BadRequestException("Can't modify user's ID");
        }

        keycloakService.updateUser(editUserRequest);
        user = mapper.editUserRequestToUser(editUserRequest);
        user.setNotificationTypes(editUserRequest.getNotificationTypes());
        userRepository.save(user);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest changePasswordRequest) {
        keycloakService.changePassword(id, changePasswordRequest);
    }

    @Override
    public void deleteUser(String id) {
        var activeReservations = this.reservationClient.countUsersFutureReservations(id, "FUTURE");
        if (activeReservations > 0) {
            throw new BadRequestException("Can't delete user that has active reservations");
        }
        accommodationClient.deleteUsersAccommodation(id);
        keycloakService.deleteUser(id);
        userRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public NotificationMessage updateHostRating(HostRatingMessage message) {
        User user = userRepository.findById(message.getHostId()).orElse(null);
        User guest = userRepository.findById(message.getGuestId()).orElse(null);

        if (user == null || guest == null) return null;

        double newRating = (user.getRatingCount() * user.getRating() + message.getRatingValue() - message.getOldRatingValue())
                / (user.getRatingCount() + 1);
        user.setRating(newRating);
        user.setRatingCount(user.getRatingCount() + 1);

        userRepository.save(user);

        if (!user.getNotificationTypes().contains(NotificationType.HOST_RATING)) return null;

        return NotificationMessage.builder()
                .createdAt(LocalDateTime.now())
                .message(String.format("User '%s' rated you", guest.getUsername()))
                .processed(!user.getNotificationTypes().contains(NotificationType.HOST_RATING))
                .notificationType(NotificationType.HOST_RATING)
                .receiverId(user.getId())
                .subjectId(user.getId())
                .build();
    }

    @Override
    public NotificationMessage checkAccomodationRating(AccomodationRatingMessage message) {
        User user = userRepository.findById(message.getHostId()).orElse(null);
        User guest = userRepository.findById(message.getGuestId()).orElse(null);

        if (user == null || guest == null) return null;

        if (!user.getNotificationTypes().contains(NotificationType.ACCOMMODATION_RATING)) return null;

        return NotificationMessage.builder()
                .notificationType(NotificationType.ACCOMMODATION_RATING)
                .message(String.format("Guest '%s' rated your accommodation!", guest.getUsername()))
                .subjectId(message.getAccomodationId())
                .receiverId(message.getHostId())
                .processed(!user.getNotificationTypes().contains(NotificationType.ACCOMMODATION_RATING))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public UserDto getById(UUID id) {
        return mapper.userToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"))
        );
    }

    @Override
    public UsersFromReservationDetails getUsersForResDetails(UUID guestId, UUID hostId) {
        var host = userRepository.findById(hostId).orElseThrow(() -> new NotFoundException("Host not found"));
        var guest = userRepository.findById(guestId).orElseThrow(() -> new NotFoundException("Guest not found"));

        return UsersFromReservationDetails.builder()
                .guestName(guest.getFirstname() + " " + guest.getLastname())
                .hostName(host.getFirstname() + " " + host.getLastname())
                .build();
    }

    @Override
    public User findUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public NotificationMessage reservationStatusUpdate(ReservationStatusUpdateMessage message) {
        User receiver = userRepository.findById(message.getReceiverId()).orElse(null);
        User sender = userRepository.findById(message.getSenderId()).orElse(null);

        if (receiver == null || sender == null) return null;

        if (!shouldNotify(receiver, message.getStatus())) return null;

        return NotificationMessage.builder()
                .createdAt(LocalDateTime.now())
                .processed(false)
                .receiverId(message.getReceiverId())
                .notificationType(setNotificationType(message.getStatus()))
                .subjectId(message.getReservationId())
                .message(createMessage(sender, message.getStatus()))
                .build();
    }

    private NotificationType setNotificationType(ReservationStatus status) {
        switch (status) {
            case CANCELED -> {
                return NotificationType.CANCELED_RESERVATION;
            }
            case WITHDRAWN -> {
                return NotificationType.WITHDRAWN_RESERVATION;
            }
            case PENDING -> {
                return NotificationType.NEW_RESERVATION;
            }
            default -> {
                return NotificationType.RESERVATION_RESPONSE;
            }
        }
    }

    private String createMessage(User sender, ReservationStatus status) {
        if (status == ReservationStatus.PENDING) {
            return String.format("User '%s' made new reservation!", sender.getUsername());
        }
        return String.format("User '%s' changed reservation status to: %s", sender.getUsername(), status);
    }

    private Boolean shouldNotify(User receiver, ReservationStatus status) {
        switch (status) {
            case CANCELED -> { return receiver.getNotificationTypes().contains(NotificationType.CANCELED_RESERVATION); }
            case WITHDRAWN -> { return receiver.getNotificationTypes().contains(NotificationType.WITHDRAWN_RESERVATION); }
            case PENDING -> { return receiver.getNotificationTypes().contains(NotificationType.NEW_RESERVATION); }
            default -> { return receiver.getNotificationTypes().contains(NotificationType.RESERVATION_RESPONSE); }
        }
    }
}
