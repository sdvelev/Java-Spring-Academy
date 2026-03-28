package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import com.digital.rewe.academy.medicalrecord.util.MonthCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {

    @Query("""
           SELECT YEAR(s.startDate) AS year, MONTH(s.startDate) AS month, COUNT(s) AS count
           FROM sick_leave s
           GROUP BY YEAR(s.startDate), MONTH(s.startDate)
           ORDER BY count DESC
           """)
    List<MonthCountProjection> findMonthWithHighestNumberOfSickLeaves();
}