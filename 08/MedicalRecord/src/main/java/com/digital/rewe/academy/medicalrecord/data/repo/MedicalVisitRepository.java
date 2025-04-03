package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCountPerDoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitsPerPatientsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {

    Long countByDoctorId(Long doctorId);

    List<MedicalVisit> findByPatientId(Long patientId);

    List<MedicalVisit> findByDateOfVisitBetween(LocalDate startDate, LocalDate endDate);

    List<MedicalVisit> findByDoctorIdAndDateOfVisitBetween(Long doctorId, LocalDate startDate, LocalDate endDate);

    @Query("""
       SELECT new com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCountPerDoctorDTO(d.id, COUNT(mv))
       FROM doctor d
       JOIN medical_visit mv ON mv.doctor.id = d.id
       GROUP BY d
       """)
    List<MedicalVisitCountPerDoctorDTO> findMedicalVisitCountPerDoctor();

    @Query("""
       SELECT new com.digital.rewe.academy.medicalrecord.dto.MedicalVisitsPerPatientsDTO(p.id, mv.id)
       FROM patient p
       JOIN medical_visit mv ON mv.patient.id = p.id
       """)
    List<MedicalVisitsPerPatientsDTO> findMedicalVisitsPerPatients();
}