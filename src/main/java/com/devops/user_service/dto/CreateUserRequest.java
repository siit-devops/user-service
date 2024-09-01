package com.devops.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Password is required")
    String password;

    @NotBlank(message = "Username is required")
    String username;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "First name is required")
    String firstname;

    @NotBlank(message = "Last name is required")
    String lastname;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "ROLE_HOST|ROLE_GUEST", message = "Role must be either ROLE_HOST or ROLE_GUEST")
    String role;

    @NotBlank(message = "Address is required")
    String address;
}
