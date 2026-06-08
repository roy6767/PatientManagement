package se.biplob.departmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.biplob.departmentservice.dtos.requestdtos.CreateTreatmentRequest;
import se.biplob.departmentservice.dtos.responsedtos.TreatmentResponse;
import se.biplob.departmentservice.exceptions.TreatmentNotFoundException;
import se.biplob.departmentservice.mapper.TreatmentMapper;
import se.biplob.departmentservice.model.Treatment;
import se.biplob.departmentservice.repository.TreatmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatmentMapper treatmentMapper;

    @Transactional
    public TreatmentResponse createTreatment(CreateTreatmentRequest request) {
        Treatment treatment = treatmentMapper.toEntity(request);
        Treatment saved = treatmentRepository.save(treatment);
        return treatmentMapper.toResponse(saved);
    }

    @Transactional
    public TreatmentResponse updateTreatment(Long id, CreateTreatmentRequest request) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new TreatmentNotFoundException(id));

        if (request.getName() != null && !request.getName().isBlank()) {
            treatment.setName(request.getName());
        }
        if (request.getDepartmentId() != null) {
            treatment.setDepartmentId(request.getDepartmentId());
        }
        if (request.getPrice() > 0) {
            treatment.setPrice(request.getPrice());
        }

        return treatmentMapper.toResponse(treatment);
    }

    public TreatmentResponse getTreatmentById(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new TreatmentNotFoundException(id));
        return treatmentMapper.toResponse(treatment);
    }

    public List<TreatmentResponse> getTreatmentsByDepartment(Long departmentId) {
        return treatmentRepository.findByDepartmentId(departmentId)
                .stream()
                .map(treatmentMapper::toResponse)
                .toList();
    }
}
