package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "doctor")
@Getter
@Setter
public class Doctor extends Person {

    @Column(name = "unique_id_number", length = 20, unique = true)
    @NotBlank(message = "The unique ID number of a doctor cannot be null, empty or blank")
    private String uniqueIdNumber;

    @ManyToMany
    @JoinTable(
        name = "doctor_has_specialty",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialtySet;

    @OneToMany(mappedBy = "doctor")
    private Set<MedicalVisit> medicalVisitSet;
}
