package com.devops.user_service.kafka;

import com.devops.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final KafkaProducer producer;
    private final UserService userService;

    @KafkaListener(topics = "host-rating", containerFactory = "hostRatingListenerContainerFactory")
    public void hostRating(HostRatingMessage message) {
        NotificationMessage notificationMessage = userService.updateHostRating(message);

        if(notificationMessage != null) {
            producer.send("notifications-topic", notificationMessage);
        }

    }

    @KafkaListener(topics = "accommodation-rating",containerFactory = "accomodationRatingListenerContainerFactory")
    public void accomodationRating(AccomodationRatingMessage message) {
        NotificationMessage notificationMessage = userService.checkAccomodationRating(message);

        if(notificationMessage != null) {
            producer.send("notifications-topic", notificationMessage);
        }
    }

    @KafkaListener(topics = "reservation-status-update", containerFactory = "reservationStatusUpdateContainer")
    public void reservationStatusUpdate(ReservationStatusUpdateMessage message)
    {
        NotificationMessage notificationMessage = userService.reservationStatusUpdate(message);

        if(notificationMessage != null) {
            producer.send("notifications-topic", notificationMessage);
        }
    }
}
