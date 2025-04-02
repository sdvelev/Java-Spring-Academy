package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.SickLeaveRepository;
import com.digital.rewe.academy.medicalrecord.exception.DoctorNotFoundException;
import com.digital.rewe.academy.medicalrecord.service.MedicalVisitService;
import com.digital.rewe.academy.medicalrecord.service.SickLeaveService;
import com.digital.rewe.academy.medicalrecord.dto.SickLeaveDTO;
import com.digital.rewe.academy.medicalrecord.exception.SickLeaveNotFoundException;
import com.digital.rewe.academy.medicalrecord.exception.UnpaidHealthInsuranceException;
import com.digital.rewe.academy.medicalrecord.mapper.MedicalVisitMapper;
import com.digital.rewe.academy.medicalrecord.mapper.SickLeaveMapper;
import com.digital.rewe.academy.medicalrecord.util.MonthCountProjection;
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
public class SickLeaveServiceImpl implements SickLeaveService {
    private final static int BOUNDARY_MONTH_PERIOD_HEALTH_INSURANCE_PAYMENTS = 6;

    private final SickLeaveRepository sickLeaveRepository;
    private final SickLeaveMapper sickLeaveMapper;

    private final MedicalVisitService medicalVisitService;
    private final MedicalVisitMapper medicalVisitMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public List<SickLeaveDTO> getSickLeaves() {
        return sickLeaveMapper.toSickLeaveDTOCollection(sickLeaveRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public SickLeaveDTO getSickLeave(Long id) {
        return sickLeaveMapper.toSickLeaveDTO(sickLeaveRepository.findById(id)
            .orElseThrow(() -> new SickLeaveNotFoundException("Such a sick leave not found", id)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public SickLeaveDTO createSickLeave(SickLeaveDTO sickLeaveDTO) {
        SickLeave sickLeaveToAdd = sickLeaveMapper.toSickLeave(sickLeaveDTO);
        MedicalVisit relevantMedicalVisit = medicalVisitMapper.toMedicalVisit(medicalVisitService
            .getMedicalVisit(sickLeaveDTO.getMedicalVisitId()));
        if (relevantMedicalVisit != null && relevantMedicalVisit.getPatient().getLastPaidHealthInsurance().isBefore(
            relevantMedicalVisit.getDateOfVisit().minusMonths(BOUNDARY_MONTH_PERIOD_HEALTH_INSURANCE_PAYMENTS))) {
            throw new UnpaidHealthInsuranceException("Sick leave cannot be issued of person with id",
                relevantMedicalVisit.getPatient().getId());
        }
        sickLeaveToAdd.setMedicalVisit(relevantMedicalVisit);
        return sickLeaveMapper.toSickLeaveDTO(sickLeaveRepository.save(sickLeaveToAdd));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public SickLeaveDTO updateSickLeave(SickLeaveDTO sickLeaveDTO, Long id) {
        Optional<SickLeave> optionalSickLeaveToUpdate = sickLeaveRepository.findById(id);
        if (optionalSickLeaveToUpdate.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalSickLeaveToUpdate.get().getMedicalVisit().getDoctor().getId().equals(currentUser.getId())) {
                throw new SickLeaveNotFoundException("Such a sick leave not found", id);
            }
            SickLeave sickLeaveToUpdate = setSickLeaveNonNullFields(sickLeaveDTO, optionalSickLeaveToUpdate.get());
            return sickLeaveMapper.toSickLeaveDTO(sickLeaveRepository.save(sickLeaveToUpdate));
        }
        throw new SickLeaveNotFoundException("Such a sick leave not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSickLeave(Long id) {
        this.sickLeaveRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public SickLeave setSickLeaveNonNullFields(SickLeaveDTO sickLeaveDTO, SickLeave sickLeaveToUpdate) {
        if (sickLeaveDTO.getStartDate() != null) {
            sickLeaveToUpdate.setStartDate(sickLeaveDTO.getStartDate());
        }

        if (sickLeaveDTO.getEndDate() != null) {
            sickLeaveToUpdate.setEndDate(sickLeaveDTO.getEndDate());
        }

        if (sickLeaveDTO.getDescription() != null) {
            sickLeaveToUpdate.setDescription(sickLeaveDTO.getDescription());
        }

        return sickLeaveToUpdate;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MonthCountProjection> getMonthWithHighestNumberOfSickLeaves() {
        return sickLeaveRepository.findMonthWithHighestNumberOfSickLeaves();
    }

    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}