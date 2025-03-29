package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import jdk.jshell.Diag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {

}
