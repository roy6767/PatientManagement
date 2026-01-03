package se.biplob.notificationservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import se.biplob.notificationservice.event.BookingCancelledEvent;
import se.biplob.notificationservice.event.BookingCreatedEvent;
import se.biplob.notificationservice.service.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "booking.created",
            groupId = "notification-service"
    )
    public void handleBookingCreated(BookingCreatedEvent event) {

        log.info("Received booking.created event for booking {}",
                event.getBookingId());

        notificationService.sendBookingConfirmation(event);
    }

    @KafkaListener(
            topics = "booking.cancelled",
            groupId = "notification-service"
    )
    public void handleBookingCancelled(
            BookingCancelledEvent event
    ) {
        log.info(
                "Received booking cancellation event for bookingId={}",
                event.getBookingId()
        );

        notificationService.sendBookingCancelledNotification(event);
    }
}

