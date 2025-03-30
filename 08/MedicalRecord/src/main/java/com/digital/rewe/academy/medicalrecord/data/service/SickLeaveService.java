package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.dto.SickLeaveDTO;
import com.digital.rewe.academy.medicalrecord.util.MonthCountProjection;

import java.util.List;

public interface SickLeaveService {
    List<SickLeaveDTO> getSickLeaves();
    SickLeaveDTO getSickLeave(Long id);
    SickLeaveDTO createSickLeave(SickLeaveDTO sickLeaveDTO);
    SickLeaveDTO updateSickLeave(SickLeaveDTO sickLeaveDTO, Long id);
    void deleteSickLeave(Long id);
    SickLeave setSickLeaveNonNullFields(SickLeaveDTO sickLeaveDTO, SickLeave sickLeaveToUpdate);
    List<MonthCountProjection> getMonthWithHighestNumberOfSickLeaves();
}

