package com.devops.user_service.service;

import com.devops.user_service.dto.CreateUserRequest;

import javax.ws.rs.core.Response;



public interface KeycloakAdminClientService {

    Response createKeycloakUser(CreateUserRequest user);
}
