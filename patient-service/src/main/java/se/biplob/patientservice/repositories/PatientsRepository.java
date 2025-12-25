package se.biplob.patientservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.patientservice.model.Patient;

import java.util.UUID;

public interface PatientsRepository extends JpaRepository<Patient, UUID> {
}
