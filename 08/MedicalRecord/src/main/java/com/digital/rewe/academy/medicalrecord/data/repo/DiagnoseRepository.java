package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    List<Diagnose> findAllByMedicalVisitSetIsNotNullOrderByMedicalVisitSetDesc();
}