package com.devops.user_service.dto;

import com.devops.user_service.kafka.enumerations.NotificationType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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

    @NotNull(message = "Notification types are required")
    List<NotificationType> notificationTypes = new ArrayList<>();
}
