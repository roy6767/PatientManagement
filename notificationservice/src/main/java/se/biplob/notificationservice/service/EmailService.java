package se.biplob.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import se.biplob.notificationservice.kafka.BookingCreatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${notification.admin.email:admin@patientmanagement.se}")
    private String adminEmail;

    public void sendBookingConfirmation(BookingCreatedEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("New Booking Confirmed — ID: " + event.getBookingId());
            message.setText(buildEmailBody(event));
            mailSender.send(message);
            log.info("Booking confirmation sent for bookingId={}", event.getBookingId());
        } catch (Exception e) {
            log.error("Failed to send email for bookingId={}: {}", event.getBookingId(), e.getMessage());
        }
    }

    private String buildEmailBody(BookingCreatedEvent event) {
        return """
                A new booking has been created in Patient Management System.

                Booking ID:    %s
                Patient ID:    %s
                Doctor ID:     %s
                Treatment ID:  %s
                Date:          %s
                Time:          %s - %s
                Amount:        SEK %.2f
                """.formatted(
                event.getBookingId(),
                event.getPatientId(),
                event.getDoctorId(),
                event.getTreatmentId(),
                event.getAppointmentDate(),
                event.getStartTime(),
                event.getEndTime(),
                event.getAmount()
        );
    }
}
