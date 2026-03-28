package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Treatment treatment = (Treatment) o;
        return Objects.equals(name, treatment.name) &&
            Objects.equals(description, treatment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}