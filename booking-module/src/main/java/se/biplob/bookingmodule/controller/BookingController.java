package se.biplob.bookingmodule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.biplob.bookingmodule.dtos.request.CreateBookingRequest;
import se.biplob.bookingmodule.dtos.response.BookingResponse;
import se.biplob.bookingmodule.service.BookingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated
//@Tag(name = "Booking", description = "API for managing bookings")
public class BookingController {

    private final BookingService bookingService;

    /* =========================
       CREATE BOOKING
       ========================= */
    @PostMapping
    //@Operation(summary = "Create a new booking")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /* =========================
       GET BOOKING BY ID
       ========================= */
    @GetMapping("/{id}")
   // @Operation(summary = "Get booking by id")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    /* =========================
       GET BOOKINGS FOR PATIENT
       ========================= */
    @GetMapping("/patient/{patientId}")
    //@Operation(summary = "Get bookings for a patient")
    public ResponseEntity<List<BookingResponse>> getPatientBookings(
            @PathVariable UUID patientId
    ) {
        return ResponseEntity.ok(
                bookingService.getPatientBookings(patientId)
        );
    }

    /* =========================
       CANCEL BOOKING
       ========================= */
    @PutMapping("/{id}/cancel")
   // @Operation(summary = "Cancel a booking")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    /* =========================
       COMPLETE BOOKING
       ========================= */
    @PutMapping("/{id}/complete")
    //@Operation(summary = "Complete a booking")
    public ResponseEntity<BookingResponse> completeBooking(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }

    /* =========================
       REBOOK
       ========================= */
    @PostMapping("/{id}/rebook")
   // @Operation(summary = "Rebook an existing booking")
    public ResponseEntity<BookingResponse> rebook(
            @PathVariable UUID id,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        return new ResponseEntity<>(
                bookingService.rebook(id, request),
                HttpStatus.CREATED
        );
    }
}

