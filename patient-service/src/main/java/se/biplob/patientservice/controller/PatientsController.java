package se.biplob.patientservice.controller;


import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.biplob.patientservice.dto.PatientsResponseDto;
import se.biplob.patientservice.exceptions.PatientNotFoundException;
import se.biplob.patientservice.model.Patient;
import se.biplob.patientservice.services.PatientsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;

@RestController
@RequestMapping("/patients")
public class PatientsController {
    private final PatientsService patientsService;
    private final ModelMapper modelMapper=new ModelMapper();

    public PatientsController(PatientsService patientsService) {
        this.patientsService = patientsService;
    }

    @GetMapping("/")
    private List<PatientsResponseDto> getPatients() {
        List<PatientsResponseDto> patients= (patientsService.getAllPatients())
                .stream()
                .map(patient ->modelMapper.map(patient, PatientsResponseDto.class))
                .toList();
        return patients;
    }

    @GetMapping("/health")
    public String health() {
        return "health is well";
    }
    @GetMapping("/{email}")
    private PatientsResponseDto getPatient(@PathVariable String id) {
        Patient patient=patientsService.getPatientById(UUID.fromString(id)).orElseThrow(()->new PatientNotFoundException("No patient found with the id"));
        return modelMapper.map(patient, PatientsResponseDto.class);
    }
}
