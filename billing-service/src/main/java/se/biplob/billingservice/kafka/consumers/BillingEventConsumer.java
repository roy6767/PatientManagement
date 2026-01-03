package se.biplob.billingservice.kafka.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.biplob.billingservice.kafka.event.BookingCreatedEvent;
import se.biplob.billingservice.model.Invoice;
import se.biplob.billingservice.model.InvoiceStatus;
import se.biplob.billingservice.repository.InvoiceRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingEventConsumer {

    private final InvoiceRepository invoiceRepository;

    @KafkaListener(
            topics = "booking.created",
            groupId = "billing-service"
    )
    @Transactional
    public void handleBookingCreated(
            BookingCreatedEvent event
    ) {
        log.info("Received booking.created event for bookingId={}",
                event.getBookingId());

        // Idempotency check
        if (invoiceRepository.existsByBookingId(event.getBookingId())) {
            log.warn("Invoice already exists for bookingId={}",
                    event.getBookingId());
            return;
        }

        Invoice invoice = Invoice.builder()
                .bookingId(event.getBookingId())
                .patientId(event.getPatientId())
                .doctorId(event.getDoctorId())
                .treatmentId(event.getTreatmentId())
                .amount(event.getAmount())
                .status(InvoiceStatus.CREATED)
                .build();

        invoiceRepository.save(invoice);

        log.info("Invoice created successfully for bookingId={}",
                event.getBookingId());
    }
}

