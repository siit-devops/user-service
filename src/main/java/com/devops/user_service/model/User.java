package com.devops.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
