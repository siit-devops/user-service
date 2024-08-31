package com.devops.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EditUserRequest {

    @NotNull
    UUID id;

    @NotNull
    String username;

    @Email
    @NotNull
    String email;

    @NotNull
    String firstname;

    @NotNull
    String lastname;

    @NotNull
    String address;
}