package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.MedicalVisitRepository;
import com.digital.rewe.academy.medicalrecord.service.DiagnoseService;
import com.digital.rewe.academy.medicalrecord.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.service.MedicalVisitService;
import com.digital.rewe.academy.medicalrecord.service.PatientService;
import com.digital.rewe.academy.medicalrecord.service.TreatmentService;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCountPerDoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitsPerPatientsDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
import com.digital.rewe.academy.medicalrecord.exception.MedicalVisitNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.DiagnoseMapper;
import com.digital.rewe.academy.medicalrecord.mapper.DoctorMapper;
import com.digital.rewe.academy.medicalrecord.mapper.MedicalVisitMapper;
import com.digital.rewe.academy.medicalrecord.mapper.PatientMapper;
import com.digital.rewe.academy.medicalrecord.mapper.TreatmentMapper;
import com.digital.rewe.academy.medicalrecord.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MedicalVisitDTO> getMedicalVisits() {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public MedicalVisitDTO getMedicalVisit(Long id) {
        User currentUser = getUserFromAuthenticationContext();
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.ADMIN.name()).build())) {
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.findById(id)
                .orElseThrow(() -> new MedicalVisitNotFoundException("Such a medical visit not found", id)));
        }

        MedicalVisit medicalVisitToReturn = medicalVisitRepository.findById(id)
            .orElseThrow(() -> new MedicalVisitNotFoundException("Such a medical visit not found", id));
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.DOCTOR.name()).build()) &&
            medicalVisitToReturn.getDoctor().getId().equals(currentUser.getId())) {
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitToReturn);
        } else if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.PATIENT.name()).build()) &&
            medicalVisitToReturn.getPatient().getId().equals(currentUser.getId())) {
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitToReturn);
        }

        throw new MedicalVisitNotFoundException("Such a medical visit not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Long getMedicalVisitsCountByDoctorId(Long doctorId) {
        return medicalVisitRepository.countByDoctorId(doctorId);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MedicalVisitCountPerDoctorDTO> getMedicalVisitsCountPerDoctor() {
        return medicalVisitRepository.findMedicalVisitCountPerDoctor();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MedicalVisitDTO> getMedicalVisitsByPatientId(Long patientId) {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository.findByPatientId(patientId));
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MedicalVisitsPerPatientsDTO> getMedicalVisitsPerPatients() {
        return medicalVisitRepository.findMedicalVisitsPerPatients();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MedicalVisitDTO> getMedicalVisitsByPeriod(LocalDate startDate, LocalDate endDate) {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository
            .findByDateOfVisitBetween(startDate, endDate));
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MedicalVisitDTO> getMedicalVisitsByPeriodAndDoctorId(Long doctorId, LocalDate startDate,
                                                                     LocalDate endDate) {
        return medicalVisitMapper.toMedicalVisitDTOCollection(medicalVisitRepository
            .findByDoctorIdAndDateOfVisitBetween(doctorId, startDate, endDate));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public MedicalVisitDTO updateMedicalVisit(MedicalVisitDTO medicalVisitDTO, Long id) {
        Optional<MedicalVisit> optionalMedicalVisitToUpdate = medicalVisitRepository.findById(id);
        if (optionalMedicalVisitToUpdate.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalMedicalVisitToUpdate.get().getDoctor().getId().equals(currentUser.getId())) {
                throw new MedicalVisitNotFoundException("Such a medical visit not found", id);
            }
            MedicalVisit medicalVisitToUpdate = setMedicalVisitNonNullFields(medicalVisitDTO, optionalMedicalVisitToUpdate.get());
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new MedicalVisitNotFoundException("Such a medical visit not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteMedicalVisit(Long id) {
        this.medicalVisitRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public MedicalVisit setMedicalVisitNonNullFields(MedicalVisitDTO medicalVisitDTO,
                                                     MedicalVisit medicalVisitToUpdate) {
        if (medicalVisitDTO.getDateOfVisit() != null) {
            medicalVisitToUpdate.setDateOfVisit(medicalVisitDTO.getDateOfVisit());
        }

        return medicalVisitToUpdate;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public MedicalVisitDTO addDiagnose(Long medicalVisitId, Long diagnoseId) {
        Optional<MedicalVisit> optionalMedicalVisitToAddDiagnose = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToAddDiagnose.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalMedicalVisitToAddDiagnose.get().getDoctor().getId().equals(currentUser.getId())) {
                throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
            }
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToAddDiagnose.get();
            DiagnoseDTO diagnoseDTO = diagnoseService.getDiagnose(diagnoseId);
            medicalVisitToUpdate.getDiagnoseSet().add(diagnoseMapper.toDiagnose(diagnoseDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public MedicalVisitDTO removeDiagnose(Long medicalVisitId, Long diagnoseId) {
        Optional<MedicalVisit> optionalMedicalVisitToRemoveDiagnose = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToRemoveDiagnose.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalMedicalVisitToRemoveDiagnose.get().getDoctor().getId().equals(currentUser.getId())) {
                throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
            }
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToRemoveDiagnose.get();
            DiagnoseDTO diagnoseDTO = diagnoseService.getDiagnose(diagnoseId);
            medicalVisitToUpdate.getDiagnoseSet().remove(diagnoseMapper.toDiagnose(diagnoseDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public MedicalVisitDTO addTreatment(Long medicalVisitId, Long treatmentId) {
        Optional<MedicalVisit> optionalMedicalVisitToAddTreatment = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToAddTreatment.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalMedicalVisitToAddTreatment.get().getDoctor().getId().equals(currentUser.getId())) {
                throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
            }
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToAddTreatment.get();
            TreatmentDTO treatmentDTO = treatmentService.getTreatment(treatmentId);
            medicalVisitToUpdate.getTreatmentSet().add(treatmentMapper.toTreatment(treatmentDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public MedicalVisitDTO removeTreatment(Long medicalVisitId, Long treatmentId) {
        Optional<MedicalVisit> optionalMedicalVisitToRemoveTreatment = medicalVisitRepository.findById(medicalVisitId);
        if (optionalMedicalVisitToRemoveTreatment.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalMedicalVisitToRemoveTreatment.get().getDoctor().getId().equals(currentUser.getId())) {
                throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
            }
            MedicalVisit medicalVisitToUpdate = optionalMedicalVisitToRemoveTreatment.get();
            TreatmentDTO treatmentDTO = treatmentService.getTreatment(treatmentId);
            medicalVisitToUpdate.getTreatmentSet().remove(treatmentMapper.toTreatment(treatmentDTO));
            return medicalVisitMapper.toMedicalVisitDTO(medicalVisitRepository.save(medicalVisitToUpdate));
        }

        throw new MedicalVisitNotFoundException("Such a medical visit not found", medicalVisitId);
    }

    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}