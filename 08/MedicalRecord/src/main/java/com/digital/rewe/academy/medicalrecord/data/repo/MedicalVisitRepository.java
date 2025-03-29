package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {

}
