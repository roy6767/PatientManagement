package se.biplob.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.biplob.notificationservice.event.BookingCancelledEvent;
import se.biplob.notificationservice.event.BookingCreatedEvent;

@Service
@Slf4j
public class NotificationService {

    public void sendBookingConfirmation(BookingCreatedEvent event) {

        // In real life → fetch patient email from cache or event itself
        log.info("""
            Sending booking confirmation
            BookingId: {}
            Appointment: {} {}-{}
            Amount: {}
        """,
                event.getBookingId(),
                event.getAppointmentDate(),
                event.getStartTime(),
                event.getEndTime(),
                event.getAmount()
        );

        // Call email / sms provider here
    }

    public void sendBookingCancelledNotification(
            BookingCancelledEvent event
    ) {
        // Email
        log.info(
                "Sending cancellation email for bookingId={}",
                event.getBookingId()
        );

        // SMS / Push (future)
    }
}

