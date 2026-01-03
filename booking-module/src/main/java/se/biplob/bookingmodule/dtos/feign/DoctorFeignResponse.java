package se.biplob.bookingmodule.dtos.feign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorFeignResponse {
    private Long id;
    private boolean active;
    private int minWeeklyBookings;
    private int maxWeeklyBookings;
}

