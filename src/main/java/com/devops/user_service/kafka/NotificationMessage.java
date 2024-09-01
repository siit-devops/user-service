package com.devops.user_service.kafka;

import com.devops.user_service.kafka.enumerations.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationMessage implements Serializable {
    private NotificationType notificationType;
    private UUID subjectId;
    private UUID receiverId;
    private String message;
    private LocalDateTime createdAt;
    private Boolean processed;
}
