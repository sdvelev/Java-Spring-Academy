package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
    Long countByDoctorId(Long doctorId);
    List<MedicalVisit> findByPatientId(Long patientId);
    List<MedicalVisit> findByDateOfVisitBetween(LocalDate startDate, LocalDate endDate);
    List<MedicalVisit> findByDoctorIdAndDateOfVisitBetween(Long doctorId, LocalDate startDate, LocalDate endDate);
}