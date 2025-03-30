package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;

import java.time.LocalDate;
import java.util.List;

public interface MedicalVisitService {
    List<MedicalVisitDTO> getMedicalVisits();
    MedicalVisitDTO getMedicalVisit(Long id);
    Long getMedicalVisitsCountByDoctorId(Long doctorId);
    List<MedicalVisitDTO> getMedicalVisitsByPatientId(Long patientId);
    List<MedicalVisitDTO> getMedicalVisitsByPeriod(LocalDate startDate, LocalDate endDate);
    List<MedicalVisitDTO> getMedicalVisitsByPeriodAndDoctorId(Long doctorId, LocalDate startDate, LocalDate endDate);
    MedicalVisitDTO createMedicalVisit(MedicalVisitCreateDTO medicalVisitCreateDTO);
    MedicalVisitDTO updateMedicalVisit(MedicalVisitDTO medicalVisitDTO, Long id);
    void deleteMedicalVisit(Long id);
    MedicalVisit setMedicalVisitNonNullFields(MedicalVisitDTO medicalVisitDTO, MedicalVisit medicalVisitToUpdate);
    MedicalVisitDTO addDiagnose(Long medicalVisitId, Long diagnoseId);
    MedicalVisitDTO removeDiagnose(Long medicalVisitId, Long diagnoseId);
    MedicalVisitDTO addTreatment(Long medicalVisitId, Long treatmentId);
    MedicalVisitDTO removeTreatment(Long medicalVisitId, Long treatmentId);
}