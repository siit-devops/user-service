package com.devops.user_service.service;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.EditUserRequest;
import com.devops.user_service.kafka.AccomodationRatingMessage;
import com.devops.user_service.kafka.HostRatingMessage;
import com.devops.user_service.kafka.NotificationMessage;
import com.devops.user_service.kafka.ReservationStatusUpdateMessage;
import com.devops.user_service.dto.UserDto;

import java.util.UUID;

public interface UserService {

    void updateUser(UUID id, EditUserRequest editUserRequest);

    void changePassword(String id, ChangePasswordRequest changePasswordRequest);

    void deleteUser(String id);

    NotificationMessage updateHostRating(HostRatingMessage message);

    NotificationMessage checkAccomodationRating(AccomodationRatingMessage message);

    NotificationMessage reservationStatusUpdate(ReservationStatusUpdateMessage message);

    UserDto getById(UUID id);
}
