package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.dto.PatientCountPerGeneralPractitionerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findDistinctByMedicalvisitSet_DiagnoseSet_Id(Long diagnoseId);

    List<Patient> findByGeneralPractitionerId(Long generalPractitionerId);

    Long countByGeneralPractitionerId(Long generalPractitionerId);

    @Query("""
       SELECT new com.digital.rewe.academy.medicalrecord.dto.PatientCountPerGeneralPractitionerDTO(gp.id, COUNT(p))
       FROM general_practitioner gp
       JOIN gp.patientSet p
       GROUP BY gp
       """)
    List<PatientCountPerGeneralPractitionerDTO> findPatientCountPerGp();
}