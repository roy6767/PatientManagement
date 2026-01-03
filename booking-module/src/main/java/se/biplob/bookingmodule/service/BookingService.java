package se.biplob.bookingmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.biplob.bookingmodule.dtos.feign.DoctorFeignResponse;
import se.biplob.bookingmodule.dtos.feign.PatientFeignResponse;
import se.biplob.bookingmodule.dtos.feign.TreatmentFeignResponse;
import se.biplob.bookingmodule.dtos.request.CreateBookingRequest;
import se.biplob.bookingmodule.dtos.response.BookingResponse;
import se.biplob.bookingmodule.exceptions.BookingNotFoundException;
import se.biplob.bookingmodule.exceptions.ExternalServiceException;
import se.biplob.bookingmodule.exceptions.InvalidBookingStateException;
import se.biplob.bookingmodule.exceptions.SlotAlreadyBookedException;
import se.biplob.bookingmodule.feignclient.DepartmentClient;
import se.biplob.bookingmodule.feignclient.PatientClient;
import se.biplob.bookingmodule.kafka.events.BookingCreatedEvent;
import se.biplob.bookingmodule.kafka.publisher.BookingEventProducer;
import se.biplob.bookingmodule.mapper.BookingMapper;
import se.biplob.bookingmodule.model.Booking;
import se.biplob.bookingmodule.model.Enum.BookingStatus;
import se.biplob.bookingmodule.repository.BookingRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PatientClient patientClient;
    private final DepartmentClient departmentClient;
    private final BookingEventProducer bookingEventProducer;

    public BookingResponse createBooking(CreateBookingRequest request) {

        PatientFeignResponse patient =
                patientClient.getPatientById(request.getPatientId());

        if (patient == null) {
            throw new ExternalServiceException("Patient not found");
        }

        DoctorFeignResponse doctor =
                departmentClient.getDoctor(request.getDoctorId());

        if (!doctor.isActive()) {
            throw new InvalidBookingStateException("Doctor is inactive");
        }

        TreatmentFeignResponse treatment =
                departmentClient.getTreatment(request.getTreatmentId());

        validateTimeRange(request.getStartTime(), request.getEndTime());
        validateSlotAvailability(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getStartTime(),
                request.getEndTime()
        );
        validateWeeklyLimits(
                doctor,
                request.getDoctorId(),
                request.getAppointmentDate()
        );

        Booking booking = bookingMapper.toEntity(request);
        booking.setStatus(BookingStatus.BOOKED);
        booking = bookingRepository.save(booking);

        bookingEventProducer.publishBookingCreated(
                BookingCreatedEvent.builder()
                        .bookingId(booking.getId())
                        .patientId(booking.getPatientId())
                        .doctorId(booking.getDoctorId())
                        .treatmentId(booking.getTreatmentId())
                        .appointmentDate(booking.getAppointmentDate())
                        .startTime(booking.getStartTime())
                        .endTime(booking.getEndTime())
                        .amount(calculateAmount(treatment))
                        .build()
        );

        return bookingMapper.toResponse(booking);
    }

    public BookingResponse cancelBooking(UUID bookingId) {

        Booking booking = getActiveBooking(bookingId);

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toResponse(booking);
    }

    public BookingResponse completeBooking(UUID bookingId) {

        Booking booking = getActiveBooking(bookingId);

        booking.setStatus(BookingStatus.COMPLETED);
        return bookingMapper.toResponse(booking);
    }


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

    private void validateWeeklyLimits(
            DoctorFeignResponse doctor,
            Long doctorId,
            LocalDate date
    ) {
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        long count = bookingRepository.countByDoctorIdAndAppointmentDateBetweenAndStatus(
                doctorId,
                weekStart,
                weekEnd,
                BookingStatus.BOOKED
        );

        if (count >= doctor.getMaxWeeklyBookings()) {
            throw new InvalidBookingStateException(
                    "Doctor has reached weekly booking limit"
            );
        }
    }
    private Double calculateAmount(TreatmentFeignResponse treatment) {
        if (treatment.getPrice() == null || treatment.getPrice() <= 0) {
            throw new InvalidBookingStateException("Invalid treatment price");
        }
        return treatment.getPrice();
    }


}
