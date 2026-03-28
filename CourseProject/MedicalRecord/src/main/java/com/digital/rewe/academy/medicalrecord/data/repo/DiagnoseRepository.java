package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    @Query("""
        SELECT d
        FROM diagnose d
        JOIN d.medicalVisitSet mv
        GROUP BY d
        ORDER BY COUNT(mv) DESC
        """)
    List<Diagnose> findDiagnosesWithFrequencyDecreasingOrder();
}