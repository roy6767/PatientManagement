package se.biplob.bookingmodule.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import se.biplob.bookingmodule.kafka.event.BookingCancelledEvent;
import se.biplob.bookingmodule.kafka.event.BookingCreatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishBookingCreated(BookingCreatedEvent event) {
        kafkaTemplate.send(
                "booking.created",
                event.getBookingId().toString(),
                event);
        log.info("Published BOOKING_CREATED event for booking {}", event.getBookingId());
    }

    public void publishBookingCancelled(BookingCancelledEvent event) {
        kafkaTemplate.send(
                "booking.cancelled",
                event.getBookingId().toString(),
                event
        );
    }
}
