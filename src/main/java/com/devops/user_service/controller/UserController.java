package com.devops.user_service.controller;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.EditUserRequest;
import com.devops.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping()
    public ResponseEntity<?> updateUser(Principal principal, @Valid @RequestBody EditUserRequest editUserRequest) {
        userService.updateUser(UUID.fromString(principal.getName()), editUserRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(principal.getName(), changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
