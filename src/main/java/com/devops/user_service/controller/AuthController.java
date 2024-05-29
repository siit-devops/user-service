package com.devops.user_service.controller;

import com.devops.user_service.config.KeycloakProvider;
import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.dto.LoginRequest;
import com.devops.user_service.service.KeycloakAdminClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
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

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {

        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(
                loginRequest.getUsername(), loginRequest.getPassword()
        ).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            log.warn("LOGIN error: Invalid account");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        } catch (NotAuthorizedException ex) {
            log.warn("LOGIN error: Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accessTokenResponse);
        }
    }

}
