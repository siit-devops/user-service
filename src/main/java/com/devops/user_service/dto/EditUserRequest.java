package com.devops.user_service.dto;

import com.devops.user_service.kafka.enumerations.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EditUserRequest {

    @NotNull(message = "ID is required")
    UUID id;

    @NotBlank(message = "Username is required")
    String username;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "First name is required")
    String firstname;

    @NotBlank(message = "Last name is required")
    String lastname;

    @NotBlank(message = "Address is required")
    String address;

    @NotNull(message = "Notification types are required")
    List<NotificationType> notificationTypes;

}