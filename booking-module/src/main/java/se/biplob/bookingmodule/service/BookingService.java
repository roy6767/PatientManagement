package se.biplob.bookingmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import se.biplob.bookingmodule.dtos.request.CreateBookingRequest;
import se.biplob.bookingmodule.dtos.response.BookingResponse;
import se.biplob.bookingmodule.exceptions.BookingNotFoundException;
import se.biplob.bookingmodule.exceptions.InvalidBookingStateException;
import se.biplob.bookingmodule.mapper.BookingMapper;
import se.biplob.bookingmodule.model.Booking;
import se.biplob.bookingmodule.model.Enum.BookingStatus;
import se.biplob.bookingmodule.repository.BookingRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    /* =========================
       CREATE BOOKING
       ========================= */
    public BookingResponse createBooking(CreateBookingRequest request) {

        validateTimeRange(request.getStartTime(), request.getEndTime());
        validateSlotAvailability(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        Booking booking = bookingMapper.toEntity(request);
        booking.setStatus(BookingStatus.BOOKED);

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    /* =========================
       CANCEL BOOKING
       ========================= */
    public BookingResponse cancelBooking(UUID bookingId) {

        Booking booking = getActiveBooking(bookingId);

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toResponse(booking);
    }

    /* =========================
       COMPLETE BOOKING
       ========================= */
    public BookingResponse completeBooking(UUID bookingId) {

        Booking booking = getActiveBooking(bookingId);

        booking.setStatus(BookingStatus.COMPLETED);
        return bookingMapper.toResponse(booking);
    }

    /* =========================
       REBOOK (cancel + create)
       ========================= */
    public BookingResponse rebook(
            UUID bookingId,
            CreateBookingRequest newRequest
    ) {

        Booking existing = getActiveBooking(bookingId);

        // Cancel old booking
        existing.setStatus(BookingStatus.CANCELLED);

        // Create new booking
        validateTimeRange(newRequest.getStartTime(), newRequest.getEndTime());
        validateSlotAvailability(
                newRequest.getDoctorId(),
                newRequest.getAppointmentDate(),
                newRequest.getStartTime(),
                newRequest.getEndTime()
        );

        Booking newBooking = bookingMapper.toEntity(newRequest);
        newBooking.setStatus(BookingStatus.BOOKED);

        bookingRepository.save(existing);
        return bookingMapper.toResponse(
                bookingRepository.save(newBooking)
        );
    }

    /* =========================
       GET BOOKINGS
       ========================= */
    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toResponse)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found: " + id)
                );
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getPatientBookings(UUID patientId) {
        return bookingRepository.findByPatientId(patientId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    /* =========================
       VALIDATIONS
       ========================= */

    private Booking getActiveBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found: " + bookingId)
                );

        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new InvalidBookingStateException(
                    "Booking is not active: " + bookingId
            );
        }
        return booking;
    }

    private void validateTimeRange(LocalTime start, LocalTime end) {
        if (!start.isBefore(end)) {
            throw new InvalidBookingStateException(
                    "Start time must be before end time"
            );
        }
    }

    private void validateSlotAvailability(
            Long doctorId,
            LocalDate date,
            LocalTime start,
            LocalTime end
    ) {

        List<Booking> existingBookings =
                bookingRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        boolean overlaps = existingBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.BOOKED)
                .anyMatch(b ->
                        start.isBefore(b.getEndTime()) &&
                                end.isAfter(b.getStartTime())
                );

        if (overlaps) {
            throw new SlotAlreadyBookedException(
                    "Doctor already has a booking in this time slot"
            );
        }
    }
}
