package se.biplob.bookingmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.biplob.bookingmodule.model.Booking;
import se.biplob.bookingmodule.model.Enum.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );

    long countByDoctorIdAndAppointmentDateBetweenAndStatus(
            Long doctorId,
            LocalDate start,
            LocalDate end,
            BookingStatus status
    );

    List<Booking> findByPatientId(UUID patientId);
}

