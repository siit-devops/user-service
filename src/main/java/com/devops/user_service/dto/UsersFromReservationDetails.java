package com.devops.user_service.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsersFromReservationDetails {
    private String hostName;
    private String guestName;
}
