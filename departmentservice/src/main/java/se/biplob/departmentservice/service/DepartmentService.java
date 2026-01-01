package se.biplob.departmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.biplob.departmentservice.dtos.requestdtos.CreateDepartmentRequest;
import se.biplob.departmentservice.dtos.responsedtos.DepartmentDetailsResponse;
import se.biplob.departmentservice.dtos.responsedtos.DepartmentResponse;
import se.biplob.departmentservice.exceptions.DepartmentNotFoundException;
import se.biplob.departmentservice.mapper.DepartmentDetailsMapper;
import se.biplob.departmentservice.mapper.DepartmentMapper;
import se.biplob.departmentservice.model.Department;
import se.biplob.departmentservice.model.Doctor;
import se.biplob.departmentservice.model.Treatment;
import se.biplob.departmentservice.repository.DepartmentRepository;
import se.biplob.departmentservice.repository.DoctorRepository;
import se.biplob.departmentservice.repository.TreatmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final TreatmentRepository treatmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentDetailsMapper detailsMapper;

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department department = departmentMapper.toEntity(request);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    @Transactional
    public DepartmentResponse updateDepartment(Long id, CreateDepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        return departmentMapper.toResponse(department);
    }

    public DepartmentDetailsResponse getDepartmentDetails(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        List<Doctor> doctors = doctorRepository.findByDepartmentIdAndActiveTrue(id);
        List<Treatment> treatments = treatmentRepository.findByDepartmentId(id);

        return detailsMapper.toResponse(department, doctors, treatments);
    }


    public List<DepartmentResponse> getActiveDepartments() {
        return departmentRepository.findAllByActiveTrue()
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }
}
