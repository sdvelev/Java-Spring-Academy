package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("""
           SELECT d
           FROM doctor d
           JOIN medical_visit mv ON mv.doctor = d
           JOIN sick_leave sl ON sl.medicalVisit = mv
           GROUP BY d
           ORDER BY COUNT(sl) DESC
           """)
    List<Doctor> findDoctorWithMostSickLeaves();
}