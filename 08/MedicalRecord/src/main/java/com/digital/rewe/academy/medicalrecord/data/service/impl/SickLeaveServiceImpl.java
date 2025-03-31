package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import com.digital.rewe.academy.medicalrecord.data.repo.SickLeaveRepository;
import com.digital.rewe.academy.medicalrecord.data.service.MedicalVisitService;
import com.digital.rewe.academy.medicalrecord.data.service.SickLeaveService;
import com.digital.rewe.academy.medicalrecord.dto.SickLeaveDTO;
import com.digital.rewe.academy.medicalrecord.mapper.MedicalVisitMapper;
import com.digital.rewe.academy.medicalrecord.mapper.SickLeaveMapper;
import com.digital.rewe.academy.medicalrecord.util.MonthCountProjection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public List<SickLeaveDTO> getSickLeaves() {
        return sickLeaveMapper.toSickLeaveDTOCollection(sickLeaveRepository.findAll());
    }

    @Override
    public SickLeaveDTO getSickLeave(Long id) {
        return sickLeaveMapper.toSickLeaveDTO(sickLeaveRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sick leave with id " + id + " not found")));
    }

    @Override
    public SickLeaveDTO createSickLeave(SickLeaveDTO sickLeaveDTO) {
        SickLeave sickLeaveToAdd = sickLeaveMapper.toSickLeave(sickLeaveDTO);
        MedicalVisit relevantMedicalVisit = medicalVisitMapper.toMedicalVisit(medicalVisitService
            .getMedicalVisit(sickLeaveDTO.getMedicalVisitId()));
        if (relevantMedicalVisit != null && relevantMedicalVisit.getPatient().getLastPaidHealthInsurance().isBefore(
            LocalDate.now().minusMonths(BOUNDARY_MONTH_PERIOD_HEALTH_INSURANCE_PAYMENTS))) {
            throw new RuntimeException("Sick leave of person with id " +
                relevantMedicalVisit.getPatient().getId() + " cannot be issued");
        }
        sickLeaveToAdd.setMedicalVisit(relevantMedicalVisit);
        return sickLeaveMapper.toSickLeaveDTO(sickLeaveRepository.save(sickLeaveToAdd));
    }

    @Override
    public SickLeaveDTO updateSickLeave(SickLeaveDTO sickLeaveDTO, Long id) {
        Optional<SickLeave> optionalSickLeaveToUpdate = sickLeaveRepository.findById(id);
        if (optionalSickLeaveToUpdate.isPresent()) {
            SickLeave sickLeaveToUpdate = setSickLeaveNonNullFields(sickLeaveDTO, optionalSickLeaveToUpdate.get());
            return sickLeaveMapper.toSickLeaveDTO(sickLeaveRepository.save(sickLeaveToUpdate));
        }

        throw new EntityNotFoundException("Sick leave with id " + id + " not found");
    }

    @Override
    public void deleteSickLeave(Long id) {
        this.sickLeaveRepository.deleteById(id);
    }

    @Override
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
    public List<MonthCountProjection> getMonthWithHighestNumberOfSickLeaves() {
        return sickLeaveRepository.findMonthWithHighestNumberOfSickLeaves();
    }
}