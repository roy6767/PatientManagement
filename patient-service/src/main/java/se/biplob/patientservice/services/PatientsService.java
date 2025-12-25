package se.biplob.patientservice.services;

import org.springframework.stereotype.Service;
import se.biplob.patientservice.model.Patient;
import se.biplob.patientservice.repositories.PatientsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientsService {
    private final PatientsRepository patientsRepository;
    public PatientsService(PatientsRepository patientsRepository) {
        this.patientsRepository = patientsRepository;
    }

    public List<Patient> getAllPatients() {
        return patientsRepository.findAll();
    }
    public Optional<Patient> getPatientById(UUID id) {
        return patientsRepository.findById(id);
    }
    public Patient createPatient(Patient patient) {
        return patientsRepository.save(patient);
    }
    public Patient updatePatient(UUID id, Patient patient) {
        return patientsRepository.save(patient);
    }
    public void deletePatient(UUID id) {
        patientsRepository.deleteById(id);
    }

}
