package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findDistinctByMedicalvisitSet_DiagnoseSet_Id(Long diagnoseId);
    List<Patient> findByGeneralPractitionerId(Long generalPractitionerId);
    Long countByGeneralPractitionerId(Long generalPractitionerId);
}