package com.my_doctor.service;

import com.my_doctor.domain.Prescription;
import com.my_doctor.model.PrescriptionDTO;
import com.my_doctor.repos.PrescriptionRepository;
import com.my_doctor.util.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(final PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<PrescriptionDTO> findAll() {
        final List<Prescription> prescriptions = prescriptionRepository.findAll(Sort.by("id"));
        return prescriptions.stream()
                .map(prescription -> mapToDTO(prescription, new PrescriptionDTO()))
                .toList();
    }

    public List<PrescriptionDTO> findAll(LocalDateTime str, LocalDateTime end) {
        final List<Prescription> prescriptions = prescriptionRepository.getAllByDateBetween(str, end);
        return prescriptions.stream()
                .map(prescription -> mapToDTO(prescription, new PrescriptionDTO()))
                .toList();
    }

    public List<PrescriptionDTO> getCurrentMonthData() {
        final List<Prescription> prescriptions = prescriptionRepository.getAllByCurrentMonth();
        return prescriptions.stream()
                .map(prescription -> mapToDTO(prescription, new PrescriptionDTO()))
                .toList();
    }

    public PrescriptionDTO get(final Long id) {
        return prescriptionRepository.findById(id)
                .map(prescription -> mapToDTO(prescription, new PrescriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PrescriptionDTO prescriptionDTO) {
        final Prescription prescription = new Prescription();
        mapToEntity(prescriptionDTO, prescription);
        return prescriptionRepository.save(prescription).getId();
    }

    public void update(final Long id, final PrescriptionDTO prescriptionDTO) {
        final Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(prescriptionDTO, prescription);
        prescriptionRepository.save(prescription);
    }

    public void delete(final Long id) {
        prescriptionRepository.deleteById(id);
    }

    private PrescriptionDTO mapToDTO(final Prescription prescription,
            final PrescriptionDTO prescriptionDTO) {
        prescriptionDTO.setId(prescription.getId());
        prescriptionDTO.setPrescriptionDate(prescription.getPrescriptionDate());
        prescriptionDTO.setName(prescription.getName());
        prescriptionDTO.setAge(prescription.getAge());
        prescriptionDTO.setGender(prescription.getGender());
        prescriptionDTO.setDiagnosis(prescription.getDiagnosis());
        prescriptionDTO.setMedicines(prescription.getMedicines());
        prescriptionDTO.setNextVisitDate(prescription.getNextVisitDate());
        return prescriptionDTO;
    }

    private Prescription mapToEntity(final PrescriptionDTO prescriptionDTO,
            final Prescription prescription) {
        prescription.setPrescriptionDate(prescriptionDTO.getPrescriptionDate());
        prescription.setName(prescriptionDTO.getName());
        prescription.setAge(prescriptionDTO.getAge());
        prescription.setGender(prescriptionDTO.getGender());
        prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
        prescription.setMedicines(prescriptionDTO.getMedicines());
        prescription.setNextVisitDate(prescriptionDTO.getNextVisitDate());
        return prescription;
    }

}
