package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;

import java.util.List;

public interface DiagnoseService {
    List<DiagnoseDTO> getDiagnoses();

    List<DiagnoseDTO> getDiagnosesByRank();

    DiagnoseDTO getDiagnose(Long id);

    DiagnoseDTO createDiagnose(DiagnoseDTO diagnoseDTO);

    DiagnoseDTO updateDiagnose(DiagnoseDTO diagnoseDTO, Long id);

    Diagnose setDiagnoseNonNullFields(DiagnoseDTO diagnoseDTO, Diagnose diagnoseToUpdate);

    void deleteDiagnose(Long id);
}