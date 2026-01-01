package se.biplob.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.departmentservice.model.DoctorTreatment;

import java.util.List;

public interface DoctorTreatmentsRepository extends JpaRepository<DoctorTreatment,Long> {
    List<DoctorTreatment> findByDoctorId(Long doctorId);

    List<DoctorTreatment> findByTreatmentId(Long treatmentId);

    boolean existsByDoctorIdAndTreatmentId(Long doctorId, Long treatmentId);
}
