package se.biplob.bookingmodule.dtos.feign;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PatientFeignResponse {
    private UUID id;
    private String name;
    private String email;
    private boolean active;
}

