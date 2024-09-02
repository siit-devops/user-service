package com.devops.user_service.model;

import com.devops.user_service.kafka.enumerations.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID id;
    @Column
    private String username;
    @Column
    private String lastname;
    @Column
    private String firstname;
    @Column
    private String email;
    @Column
    private String address;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    @ElementCollection(targetClass = NotificationType.class, fetch = FetchType.EAGER)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    List<NotificationType> notificationTypes = new ArrayList<NotificationType>();
    @Column
    private Integer ratingCount = 0;
    @Column
    private Double rating = 0.0;
}
