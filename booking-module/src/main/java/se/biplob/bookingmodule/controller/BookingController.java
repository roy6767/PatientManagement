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

public class BookingController {

    private final BookingService bookingService;


    @PostMapping

    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BookingResponse>> getPatientBookings(
            @PathVariable UUID patientId
    ) {
        return ResponseEntity.ok(
                bookingService.getPatientBookings(patientId)
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }


    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> completeBooking(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }


    @PostMapping("/{id}/rebook")
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

