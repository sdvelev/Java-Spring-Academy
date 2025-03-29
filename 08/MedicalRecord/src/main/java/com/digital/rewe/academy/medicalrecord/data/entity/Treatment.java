package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "treatment")
@Getter
@Setter
public class Treatment extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message = "The name of a treatment cannot be null, empty or blank")
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToMany(mappedBy = "treatmentSet")
    private Set<MedicalVisit> medicalVisitSet;
}