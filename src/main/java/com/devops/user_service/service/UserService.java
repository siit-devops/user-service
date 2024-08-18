package com.devops.user_service.service;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.EditUserRequest;

import java.util.UUID;

public interface UserService {

    void updateUser(UUID id, EditUserRequest editUserRequest);

    void changePassword(String id, ChangePasswordRequest changePasswordRequest);

    void deleteUser(String id);
}
