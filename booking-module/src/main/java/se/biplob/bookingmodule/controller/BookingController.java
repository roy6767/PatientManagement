package se.biplob.bookingmodule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.biplob.bookingmodule.model.Booking;
import se.biplob.bookingmodule.model.BookingRequest;
import se.biplob.bookingmodule.service.BookingService;

import java.util.UUID;

@RestController
@RequestMapping("/api/book")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> addBooking(@RequestBody BookingRequest request) {
        return bookingService.issueBooking(request);
    }
    @GetMapping
    public ResponseEntity<?> getBooking(@RequestParam UUID id) {
        return bookingService.getBookingByID(id);
    }
}
