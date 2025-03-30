package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    List<PatientDTO> getPatients();
    PatientDTO getPatient(Long id);
    //    DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO);
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO createPatientWithExistingAddress(PatientDTO patientDTO, Long addressId);
    PatientDTO updatePatient(PatientDTO patientDTO, Long id);
    void deletePatient(Long id);
    Patient setPatientNonNullFields(PatientDTO patientDTO, Patient patientToUpdate);

    PatientDTO addGeneralPractitioner(Long patientId, Long generalPractitionerId);
    PatientDTO removeGeneralPractitioner(Long patientId);
}