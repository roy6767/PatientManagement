package se.biplob.bookingmodule.kafka.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import se.biplob.bookingmodule.kafka.events.BookingCreatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "booking.created";

    public void publishBookingCreated(BookingCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getBookingId().toString(), event);
        log.info("Published BOOKING_CREATED event for booking {}", event.getBookingId());
    }
}
