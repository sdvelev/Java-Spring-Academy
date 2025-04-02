package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.dto.PatientCountPerGeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    List<PatientDTO> getPatients();

    List<PatientDTO> getPatientsWithSpecificDiagnose(Long diagnoseId);

    List<PatientDTO> getPatientsWithGeneralPractitioner(Long generalPractitionerId);

    Long getCountByGeneralPractitionerId(Long generalPractitionerId);

    List<PatientCountPerGeneralPractitionerDTO> getCountPerGeneralPractitioner();

    PatientDTO getPatient(Long id);

    PatientDTO createPatient(PatientCreateDTO patientCreateDTO);

//    PatientDTO createPatientWithExistingAddress(PatientDTO patientDTO, Long addressId);

    PatientDTO updatePatient(PatientDTO patientDTO, Long id);

    Patient setPatientNonNullFields(PatientDTO patientDTO, Patient patientToUpdate);

    void deletePatient(Long id);

    PatientDTO addGeneralPractitioner(Long patientId, Long generalPractitionerId);

    PatientDTO removeGeneralPractitioner(Long patientId);
}