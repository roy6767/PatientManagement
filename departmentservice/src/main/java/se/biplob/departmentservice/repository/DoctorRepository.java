package se.biplob.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.departmentservice.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    List<Doctor> findByDepartmentIdAndActiveTrue(Long departmentId);

    Optional<Doctor> findByEmail(String email);
}
