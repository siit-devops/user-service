package com.devops.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required!")
    private String oldPassword;
    @NotBlank(message = "New password is required")
    private String newPassword;

}
