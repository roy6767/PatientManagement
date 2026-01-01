package se.biplob.departmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.biplob.departmentservice.dtos.requestdtos.CreateDoctorRequest;
import se.biplob.departmentservice.dtos.requestdtos.UpdateDoctorRequest;
import se.biplob.departmentservice.dtos.responsedtos.DoctorResponse;
import se.biplob.departmentservice.dtos.responsedtos.DoctorWithTreatmentsResponse;
import se.biplob.departmentservice.exceptions.DoctorNotFoundException;
import se.biplob.departmentservice.mapper.DoctorMapper;
import se.biplob.departmentservice.mapper.DoctorWithTreatmentsMapper;
import se.biplob.departmentservice.mapper.TreatmentMapper;
import se.biplob.departmentservice.model.Doctor;
import se.biplob.departmentservice.model.DoctorTreatment;
import se.biplob.departmentservice.model.Treatment;
import se.biplob.departmentservice.repository.DoctorRepository;
import se.biplob.departmentservice.repository.DoctorTreatmentsRepository;
import se.biplob.departmentservice.repository.TreatmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorTreatmentsRepository doctorTreatmentsRepository;
    private final TreatmentRepository treatmentRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorWithTreatmentsMapper doctorWithTreatmentsMapper;
    private final TreatmentMapper treatmentMapper;

    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        Doctor doctor = doctorMapper.toEntity(request);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toResponse(saved);
    }

    @Transactional
    public DoctorResponse updateDoctor(Long id, UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        if (request.getFirstName() != null) doctor.setFirstName(request.getFirstName());
        if (request.getLastName() != null) doctor.setLastName(request.getLastName());
        if (request.getEmail() != null) doctor.setEmail(request.getEmail());
        if (request.getDesignation() != null) doctor.setDesignation(request.getDesignation());
        if (request.getAbout() != null) doctor.setAbout(request.getAbout());
        if (request.getMinWeeklyBookings() != null) doctor.setMinWeeklyBookings(request.getMinWeeklyBookings());
        if (request.getMaxWeeklyBookings() != null) doctor.setMaxWeeklyBookings(request.getMaxWeeklyBookings());
        if (request.getActive() != null) doctor.setActive(request.getActive());

        return doctorMapper.toResponse(doctor);
    }

    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentIdAndActiveTrue(departmentId)
                .stream()
                .map(doctorMapper::toResponse)
                .toList();
    }

    public DoctorWithTreatmentsResponse getDoctorWithTreatments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));

        List<Long> treatmentIds = doctorTreatmentsRepository.findByDoctorId(doctorId)
                .stream()
                .map(DoctorTreatment::getTreatmentId)
                .toList();

        List<Treatment> treatments = treatmentRepository.findAllById(treatmentIds);

        return doctorWithTreatmentsMapper.toResponse(doctor, treatments);
    }
}

