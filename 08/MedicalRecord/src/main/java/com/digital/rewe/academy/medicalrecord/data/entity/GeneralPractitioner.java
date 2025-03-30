package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "general_practitioner")
@Getter
@Setter
public class GeneralPractitioner extends Doctor {

    @Column(name = "practice_number", length = 20)
    @NotBlank(message = "The practice number of a general practitioner cannot be null, empty or blank")
    private String practiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practice_address_id", nullable = false)
    private Address practiceAddress;

    @OneToMany(mappedBy = "generalPractitioner")
    private Set<Patient> patientSet;
}