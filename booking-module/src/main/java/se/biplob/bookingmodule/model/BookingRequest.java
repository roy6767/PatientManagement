package se.biplob.bookingmodule.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BookingRequest {
    private UUID patientId;
}
