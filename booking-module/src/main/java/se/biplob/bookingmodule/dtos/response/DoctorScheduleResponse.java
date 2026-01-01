package se.biplob.bookingmodule.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DoctorScheduleResponse {

    private Long doctorId;
    private LocalDate date;
    private List<TimeSlotResponse> bookedSlots;
}
