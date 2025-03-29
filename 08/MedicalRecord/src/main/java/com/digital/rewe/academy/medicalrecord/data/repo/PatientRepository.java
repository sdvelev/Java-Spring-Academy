package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
