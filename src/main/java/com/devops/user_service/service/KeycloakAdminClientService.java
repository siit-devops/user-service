package com.devops.user_service.service;

import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.model.User;

import javax.ws.rs.core.Response;


public interface KeycloakAdminClientService {

    Response createKeycloakUser(CreateUserRequest user);

    void updateUser(CreateUserRequest editUserRequest);
}
