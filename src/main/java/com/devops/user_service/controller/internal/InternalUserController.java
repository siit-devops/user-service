package com.devops.user_service.controller.internal;


import com.devops.user_service.dto.UserDto;
import com.devops.user_service.dto.UsersFromReservationDetails;
import com.devops.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/{id}")
    UserDto findById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @GetMapping("/host-and-guest-details")
    UsersFromReservationDetails getUsersForReservationDetails(@RequestParam UUID guestId, @RequestParam UUID hostId) {
        return userService.getUsersForResDetails(guestId, hostId);
    }


}
