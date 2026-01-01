package se.biplob.departmentservice.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String designation;
    private String about;
    private String email;

    private int minWeeklyBookings;
    private int maxWeeklyBookings;
    private boolean active;
}
