package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "patient")
@Getter
@Setter
public class Patient extends Person {

    @Column(name = "last_paid_health_insurance")
    @PastOrPresent(message = "The last paid health insurance date of a patient cannot be in the future")
    private LocalDate lastPaidHealthInsurance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_practitioner_id")
    private GeneralPractitioner generalPractitioner;

    @OneToMany(mappedBy = "patient")
    private Set<MedicalVisit> medicalvisitSet;
}