package com.devops.user_service.service.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "accommodation-service", url = "${accommodation-service.ribbon.listOfServers}")
public interface AccommodationClient {

    @DeleteMapping("/api/internal/accommodations/delete-users-accommodations/{userId}")
    void deleteUsersAccommodation(@PathVariable String userId);

}

