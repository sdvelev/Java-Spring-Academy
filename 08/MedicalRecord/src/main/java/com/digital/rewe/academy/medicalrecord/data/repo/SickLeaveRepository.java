package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
}
