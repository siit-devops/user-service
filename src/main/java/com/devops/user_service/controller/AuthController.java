package com.devops.user_service.controller;

import com.devops.user_service.config.KeycloakProvider;
import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.service.KeycloakAdminClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
@Validated
public class AuthController {

    private final KeycloakAdminClientService kcAdminClient;
    private final KeycloakProvider kcProvider;

    @PostMapping(value = "/registration")
    public ResponseEntity<Response> addUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        var createdResponse = kcAdminClient.createKeycloakUser(createUserRequest);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }
}
