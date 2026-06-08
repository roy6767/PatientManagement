package se.biplob.bookingmodule.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import se.biplob.bookingmodule.dtos.feign.PatientFeignResponse;
import se.biplob.bookingmodule.exceptions.ExternalServiceException;

import java.util.UUID;

@Component
@Slf4j
public class PatientClientFallbackFactory implements FallbackFactory<PatientClient> {

    @Override
    public PatientClient create(Throwable cause) {
        return new PatientClient() {
            @Override
            public PatientFeignResponse getPatientById(UUID id) {
                log.error("patient-service circuit breaker OPEN for patientId={}: {}", id, cause.getMessage());
                throw new ExternalServiceException(
                        "Patient service is currently unavailable. Please try again later.", cause);
            }
        };
    }
}
