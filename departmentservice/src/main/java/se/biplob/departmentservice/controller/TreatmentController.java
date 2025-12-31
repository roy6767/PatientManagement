package se.biplob.departmentservice.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.biplob.departmentservice.dtos.requestdtos.CreateTreatmentRequest;
import se.biplob.departmentservice.dtos.responsedtos.TreatmentResponse;
import se.biplob.departmentservice.service.TreatmentService;

import java.util.List;


@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @PostMapping
    public ResponseEntity<TreatmentResponse> createTreatment(
            @Valid @RequestBody CreateTreatmentRequest request) {
        TreatmentResponse response = treatmentService.createTreatment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResponse> updateTreatment(
            @PathVariable Long id,
            @Valid @RequestBody CreateTreatmentRequest request) {
        TreatmentResponse response = treatmentService.updateTreatment(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<TreatmentResponse>> getTreatmentsByDepartment(@PathVariable Long departmentId) {
        List<TreatmentResponse> response = treatmentService.getTreatmentsByDepartment(departmentId);
        return ResponseEntity.ok(response);
    }
}

