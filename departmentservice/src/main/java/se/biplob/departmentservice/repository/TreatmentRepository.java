package se.biplob.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.departmentservice.model.Treatment;

import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment,Long> {
    List<Treatment> findByDepartmentId(Long departmentId);
}
