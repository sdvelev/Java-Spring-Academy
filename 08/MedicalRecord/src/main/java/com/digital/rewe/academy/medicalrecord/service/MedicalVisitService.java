package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCountPerDoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitsPerPatientsDTO;

import java.time.LocalDate;
import java.util.List;

public interface MedicalVisitService {

    List<MedicalVisitDTO> getMedicalVisits();

    MedicalVisitDTO getMedicalVisit(Long id);

    Long getMedicalVisitsCountByDoctorId(Long doctorId);

    List<MedicalVisitCountPerDoctorDTO> getMedicalVisitsCountPerDoctor();

    List<MedicalVisitDTO> getMedicalVisitsByPatientId(Long patientId);

    List<MedicalVisitsPerPatientsDTO> getMedicalVisitsPerPatients();

    List<MedicalVisitDTO> getMedicalVisitsByPeriod(LocalDate startDate, LocalDate endDate);

    List<MedicalVisitDTO> getMedicalVisitsByPeriodAndDoctorId(Long doctorId, LocalDate startDate, LocalDate endDate);

    MedicalVisitDTO createMedicalVisit(MedicalVisitCreateDTO medicalVisitCreateDTO);

    MedicalVisitDTO updateMedicalVisit(MedicalVisitDTO medicalVisitDTO, Long id);

    MedicalVisit setMedicalVisitNonNullFields(MedicalVisitDTO medicalVisitDTO, MedicalVisit medicalVisitToUpdate);

    void deleteMedicalVisit(Long id);

    MedicalVisitDTO addDiagnose(Long medicalVisitId, Long diagnoseId);

    MedicalVisitDTO removeDiagnose(Long medicalVisitId, Long diagnoseId);

    MedicalVisitDTO addTreatment(Long medicalVisitId, Long treatmentId);

    MedicalVisitDTO removeTreatment(Long medicalVisitId, Long treatmentId);
}