package se.biplob.bookingmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.bookingmodule.model.Booking;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
