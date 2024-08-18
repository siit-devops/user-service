package com.devops.user_service.service;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.dto.EditUserRequest;
import com.devops.user_service.model.User;

import javax.ws.rs.core.Response;


public interface KeycloakAdminClientService {

    Response createKeycloakUser(CreateUserRequest user);

    void updateUser(EditUserRequest editUserRequest);

    void changePassword(String id, ChangePasswordRequest changePasswordRequest);

    void deleteUser(String id);
}
