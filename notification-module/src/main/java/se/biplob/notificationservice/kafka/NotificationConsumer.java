package se.biplob.notificationservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.biplob.notificationservice.service.EmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "booking.created",
            groupId = "notification-module",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info("Received booking.created event for bookingId={}", event.getBookingId());
        emailService.sendBookingConfirmation(event);
    }
}
