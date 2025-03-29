package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "sick_leave")
@Getter
@Setter
public class SickLeave extends BaseEntity {

    @Column(name = "start_date")
    @NotNull(message = "The start date of a sick leave cannot be null")
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull(message = "The end date of a sick leave cannot be null")
    private LocalDate endDate;

    @Column(name = "description", length = 500)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_visit_id", nullable = false)
    private MedicalVisit medicalVisit;

}
