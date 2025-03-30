package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity(name = "specialty")
@Getter
@Setter
public class Specialty extends BaseEntity {

    @Column(name = "specialty_name")
    @NotBlank(message = "The name of a specialty cannot be null, empty or blank")
    private String specialtyName;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "specialtySet")
    private Set<Doctor> doctorSet;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Specialty specialty = (Specialty) o;
        return Objects.equals(specialtyName, specialty.specialtyName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(specialtyName);
    }
}