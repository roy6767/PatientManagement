package se.biplob.bookingmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.biplob.bookingmodule.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );

    @Query("""
        SELECT COUNT(b)
        FROM Booking b
        WHERE b.doctorId = :doctorId
          AND b.status = 'BOOKED'
          AND b.appointmentDate BETWEEN :start AND :end
    """)
    long countWeeklyBookings(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    List<Booking> findByPatientId(UUID patientId);
}

