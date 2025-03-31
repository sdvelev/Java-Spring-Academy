package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;

import java.util.List;

public interface TreatmentService {
    List<TreatmentDTO> getTreatments();

    TreatmentDTO getTreatment(Long id);

    TreatmentDTO createTreatment(TreatmentDTO treatmentDTO);

    TreatmentDTO updateTreatment(TreatmentDTO treatmentDTO, Long id);

    Treatment setTreatmentNonNullFields(TreatmentDTO treatmentDTO, Treatment treatmentToUpdate);

    void deleteTreatment(Long id);
}