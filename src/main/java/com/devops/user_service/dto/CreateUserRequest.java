package com.devops.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateUserRequest {

    UUID id;

    @NotNull
    String password;

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
    @Pattern(regexp = "ROLE_HOST|ROLE_GUEST", message = "Role must be either ROLE_HOST or ROLE_GUEST")
    String role;

    @NotNull
    AddressDto address;
}
