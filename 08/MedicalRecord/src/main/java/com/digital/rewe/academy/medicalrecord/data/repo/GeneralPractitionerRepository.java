package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralPractitionerRepository extends JpaRepository<GeneralPractitioner, Long> {
}
