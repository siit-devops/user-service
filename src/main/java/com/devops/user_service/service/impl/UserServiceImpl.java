package com.devops.user_service.service.impl;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.EditUserRequest;
import com.devops.user_service.exception.BadRequestException;
import com.devops.user_service.exception.NotFoundException;
import com.devops.user_service.mappers.MapStructMapper;
import com.devops.user_service.model.User;
import com.devops.user_service.repository.UserRepository;
import com.devops.user_service.service.KeycloakAdminClientService;
import com.devops.user_service.service.UserService;
import com.devops.user_service.service.feignClients.ReservationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminClientService keycloakService;
    private final MapStructMapper mapper;

    private final ReservationClient reservationClient;

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
        // todo:
        //  in accommodation service:
        //  - delete all host's accommodations
        keycloakService.deleteUser(id);
        userRepository.deleteById(UUID.fromString(id));
    }

}
