package com.devops.user_service.controller;

import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping()
    public ResponseEntity<?> updateUser(Principal principal, @RequestBody CreateUserRequest editUserRequest) {
        userService.updateUser(UUID.fromString(principal.getName()), editUserRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
