package com.devops.user_service.service.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reservation-service", url = "${reservation-service.ribbon.listOfServers}")
public interface ReservationClient {

    @GetMapping("/api/internal/reservations/{userId}/count")
    int countUsersFutureReservations(@PathVariable String userId, @RequestParam String reservationPeriod);

}
