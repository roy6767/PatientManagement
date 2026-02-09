package se.biplob.patientservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.biplob.patientservice.dto.PatientRequest;
import se.biplob.patientservice.dto.PatientResponse;
import se.biplob.patientservice.model.Patient;
import se.biplob.patientservice.services.PatientService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Validated
@Tag(name="Patient",description = "API for managing Patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(summary="Get Patients")
    public ResponseEntity<Page<PatientResponse>> getPatients(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(patientService.getPatients(name, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary="Get Patient by id")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PostMapping
    @Operation(summary="Create Patient")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary="Update Patient by id")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable UUID id,@Valid @RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.update(id,request), HttpStatus.OK);
     }


    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.findByEmail(email);
     }
}