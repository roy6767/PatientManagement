package se.biplob.departmentservice.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.biplob.departmentservice.dtos.requestdtos.CreateDoctorRequest;
import se.biplob.departmentservice.dtos.requestdtos.UpdateDoctorRequest;
import se.biplob.departmentservice.dtos.responsedtos.DoctorResponse;
import se.biplob.departmentservice.dtos.responsedtos.DoctorWithTreatmentsResponse;
import se.biplob.departmentservice.service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request) {
        DoctorResponse response = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorRequest request) {
        DoctorResponse response = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByDepartment(@PathVariable Long departmentId) {
        List<DoctorResponse> response = doctorService.getDoctorsByDepartment(departmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{doctorId}/treatments")
    public ResponseEntity<DoctorWithTreatmentsResponse> getDoctorWithTreatments(@PathVariable Long doctorId) {
        DoctorWithTreatmentsResponse response = doctorService.getDoctorWithTreatments(doctorId);
        return ResponseEntity.ok(response);
    }
}

