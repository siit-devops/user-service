package com.devops.user_service.service;

import com.devops.user_service.dto.CreateUserRequest;

import java.util.UUID;

public interface UserService {

    void updateUser(UUID id, CreateUserRequest editUserRequest);
}
