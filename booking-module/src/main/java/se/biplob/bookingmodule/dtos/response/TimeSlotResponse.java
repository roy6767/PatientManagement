package se.biplob.bookingmodule.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class TimeSlotResponse {
    private LocalTime startTime;
    private LocalTime endTime;
}
