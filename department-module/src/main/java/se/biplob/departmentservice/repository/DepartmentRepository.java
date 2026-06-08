package se.biplob.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.departmentservice.model.Department;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Optional<Department> findByNameAndActiveTrue(String name);

    List<Department> findAllByActiveTrue();

}
