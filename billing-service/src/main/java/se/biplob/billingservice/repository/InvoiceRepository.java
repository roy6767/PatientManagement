package se.biplob.billingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.billingservice.model.Invoice;
import java.util.UUID;
import java.util.Optional;


public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByBookingId(UUID bookingId);
}
