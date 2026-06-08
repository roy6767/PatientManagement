package se.biplob.departmentservice.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDoctorRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String designation;
    private String about;
    private Integer minWeeklyBookings;
    private Integer maxWeeklyBookings;
    private Boolean active;
}

