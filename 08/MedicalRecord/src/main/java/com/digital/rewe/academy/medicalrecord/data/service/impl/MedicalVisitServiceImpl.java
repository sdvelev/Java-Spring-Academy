package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.data.repo.MedicalVisitRepository;
import com.digital.rewe.academy.medicalrecord.data.service.DiagnoseService;
import com.digital.rewe.academy.medicalrecord.data.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.data.service.MedicalVisitService;
import com.digital.rewe.academy.medicalrecord.data.service.PatientService;
import com.digital.rewe.academy.medicalrecord.data.service.TreatmentService;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
import com.digital.rewe.academy.medicalrecord.mapper.DiagnoseMapper;
import com.digital.rewe.academy.medicalrecord.mapper.DoctorMapper;
import com.digital.rewe.academy.medicalrecord.mapper.MedicalVisitMapper;
import com.digital.rewe.academy.medicalrecord.mapper.PatientMapper;
import com.digital.rewe.academy.medicalrecord.mapper.TreatmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalVisitServiceImpl implements MedicalVisitService {

    private final MedicalVisitRepository medicalVisitRepository;
    private final MedicalVisitMapper medicalVisitMapper;

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    private final DiagnoseService diagnoseService;
    private final DiagnoseMapper diagnoseMapper;

    private final TreatmentService treatmentService;
    private final TreatmentMapper treatmentMapper;

    @Override
    public List<MedicalVisitDTO> getMedicalVisits() {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository.findAll());
    }

    @Override
    public MedicalVisitDTO getMedicalVisit(Long id) {
        return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Medical visit with id " + id + " not found")));
    }

    @Override
    public Long getMedicalVisitsCountByDoctorId(Long doctorId) {
        return medicalVisitRepository.countByDoctorId(doctorId);
    }

    @Override
    public List<MedicalVisitDTO> getMedicalVisitsByPatientId(Long patientId) {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository.findByPatientId(patientId));
    }

    @Override
    public List<MedicalVisitDTO> getMedicalVisitsByPeriod(LocalDate startDate, LocalDate endDate) {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository
            .findByDateOfVisitBetween(startDate, endDate));
    }

    @Override
    public List<MedicalVisitDTO> getMedicalVisitsByPeriodAndDoctorId(Long doctorId, LocalDate startDate,
                                                                     LocalDate endDate) {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository
            .findByDoctorIdAndDateOfVisitBetween(doctorId, startDate, endDate));
    }

    @Override
    public MedicalVisitDTO createMedicalVisit(MedicalVisitCreateDTO medicalVisitCreateDTO) {
        MedicalVisit medicalVisitToSave = new MedicalVisit();

        PatientDTO patientDTOToAdd = patientService.getPatient(medicalVisitCreateDTO.getPatientId());
        DoctorDTO doctorDTOToAdd = doctorService.getDoctor(medicalVisitCreateDTO.getDoctorId());

        medicalVisitToSave.setDateOfVisit(medicalVisitCreateDTO.getDateOfVisit());
        medicalVisitToSave.setPatient(patientMapper.toPatient(patientDTOToAdd));
        medicalVisitToSave.setDoctor(doctorMapper.toDoctor(doctorDTOToAdd));

        return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToSave));
    }

    @Override
    public MedicalVisitDTO updateMedicalVisit(MedicalVisitDTO medicalVisitDTO, Long id) {
        Optional<MedicalVisit> optionalMedicalVisitToUpdate = medicalVisitRepository.findById(id);
        if (optionalMedicalVisitToUpdate.isPresent()) {
            MedicalVisit medicalVisitToUpdate = setMedicalVisitNonNullFields(medicalVisitDTO, optionalMedicalVisitToUpdate.get());
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new EntityNotFoundException("Medical visit with id " + id + " not found");
    }

    @Override
    public void deleteMedicalVisit(Long id) {
        this.medicalVisitRepository.deleteById(id);
    }

    @Override
    public MedicalVisit setMedicalVisitNonNullFields(MedicalVisitDTO medicalVisitDTO,
                                                     MedicalVisit medicalVisitToUpdate) {
        if (medicalVisitDTO.getDateOfVisit() != null) {
            medicalVisitToUpdate.setDateOfVisit(medicalVisitDTO.getDateOfVisit());
        }

        return medicalVisitToUpdate;
    }

    @Override
    public MedicalVisitDTO addDiagnose(Long medicalVisitId, Long diagnoseId) {
        Optional<MedicalVisit> optionalMedicalVisitToAddDiagnose = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToAddDiagnose.isPresent()) {
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToAddDiagnose.get();
            DiagnoseDTO diagnoseDTO = diagnoseService.getDiagnose(diagnoseId);
            medicalVisitToUpdate.getDiagnoseSet().add(diagnoseMapper.toDiagnose(diagnoseDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new EntityNotFoundException("Medical visit with id " + medicalVisitId + " not found");
    }

    @Override
    public MedicalVisitDTO removeDiagnose(Long medicalVisitId, Long diagnoseId) {
        Optional<MedicalVisit> optionalMedicalVisitToRemoveDiagnose = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToRemoveDiagnose.isPresent()) {
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToRemoveDiagnose.get();
            DiagnoseDTO diagnoseDTO = diagnoseService.getDiagnose(diagnoseId);
            medicalVisitToUpdate.getDiagnoseSet().remove(diagnoseMapper.toDiagnose(diagnoseDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new EntityNotFoundException("Medical visit with id " + medicalVisitId + " not found");
    }

    @Override
    public MedicalVisitDTO addTreatment(Long medicalVisitId, Long treatmentId) {
        Optional<MedicalVisit> optionalMedicalVisitToAddTreatment = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToAddTreatment.isPresent()) {
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToAddTreatment.get();
            TreatmentDTO treatmentDTO = treatmentService.getTreatment(treatmentId);
            medicalVisitToUpdate.getTreatmentSet().add(treatmentMapper.toTreatment(treatmentDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new EntityNotFoundException("Medical visit with id " + medicalVisitId + " not found");
    }

    @Override
    public MedicalVisitDTO removeTreatment(Long medicalVisitId, Long treatmentId) {
        Optional<MedicalVisit> optionalMedicalVisitToRemoveTreatment = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToRemoveTreatment.isPresent()) {
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToRemoveTreatment.get();
            TreatmentDTO treatmentDTO = treatmentService.getTreatment(treatmentId);
            medicalVisitToUpdate.getTreatmentSet().remove(treatmentMapper.toTreatment(treatmentDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new EntityNotFoundException("Medical visit with id " + medicalVisitId + " not found");
    }
}