package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}
