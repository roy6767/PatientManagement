package se.biplob.bookingmodule.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.biplob.bookingmodule.dtos.feign.PatientFeignResponse;

import java.util.UUID;

@FeignClient(
        name = "patient-service",
        url = "${services.patient.url}"
)
public interface PatientClient {

    @GetMapping("/api/v1/patients/{id}")
    PatientFeignResponse getPatientById(@PathVariable UUID id);
}

