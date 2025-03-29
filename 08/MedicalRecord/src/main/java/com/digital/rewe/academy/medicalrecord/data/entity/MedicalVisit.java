package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "medical_visit")
@Getter
@Setter
public class MedicalVisit extends BaseEntity{

    @Column(name = "date_of_visit")
    @NotNull(message = "The date of a medical visit cannot be null")
    @PastOrPresent(message = "The date of a medical visit cannot be in the future")
    private LocalDate dateOfVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToMany
    @JoinTable(
        name = "medical_visit_has_diagnose",
        joinColumns = @JoinColumn(name = "medical_visit_id"),
        inverseJoinColumns = @JoinColumn(name = "diagnose_id"))
    private Set<Diagnose> diagnoseSet;

    @ManyToMany
    @JoinTable(
        name = "medical_visit_has_treatment",
        joinColumns = @JoinColumn(name = "medical_visit_id"),
        inverseJoinColumns = @JoinColumn(name = "treatment_id"))
    private Set<Treatment> treatmentSet;

    @OneToOne(mappedBy = "medicalVisit")
    private SickLeave sickLeave;
}