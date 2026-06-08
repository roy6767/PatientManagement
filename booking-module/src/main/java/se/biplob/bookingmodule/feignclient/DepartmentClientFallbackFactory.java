package se.biplob.bookingmodule.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import se.biplob.bookingmodule.dtos.feign.DoctorFeignResponse;
import se.biplob.bookingmodule.dtos.feign.TreatmentFeignResponse;
import se.biplob.bookingmodule.exceptions.ExternalServiceException;

@Component
@Slf4j
public class DepartmentClientFallbackFactory implements FallbackFactory<DepartmentClient> {

    @Override
    public DepartmentClient create(Throwable cause) {
        return new DepartmentClient() {
            @Override
            public DoctorFeignResponse getDoctor(Long id) {
                log.error("departmentservice circuit breaker OPEN for doctorId={}: {}", id, cause.getMessage());
                throw new ExternalServiceException(
                        "Department service is currently unavailable. Please try again later.", cause);
            }

            @Override
            public TreatmentFeignResponse getTreatment(Long id) {
                log.error("departmentservice circuit breaker OPEN for treatmentId={}: {}", id, cause.getMessage());
                throw new ExternalServiceException(
                        "Department service is currently unavailable. Please try again later.", cause);
            }
        };
    }
}
