package se.biplob.bookingmodule.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.biplob.bookingmodule.dtos.feign.DoctorFeignResponse;
import se.biplob.bookingmodule.dtos.feign.TreatmentFeignResponse;

@FeignClient(name = "department-module", fallbackFactory = DepartmentClientFallbackFactory.class)
public interface DepartmentClient {

    @GetMapping("/api/doctors/{id}")
    DoctorFeignResponse getDoctor(@PathVariable Long id);

    @GetMapping("/api/treatments/{id}")
    TreatmentFeignResponse getTreatment(@PathVariable Long id);
}

