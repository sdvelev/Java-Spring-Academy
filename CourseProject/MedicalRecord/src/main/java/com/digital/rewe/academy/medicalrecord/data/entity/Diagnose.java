package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity(name = "diagnose")
@Getter
@Setter
public class Diagnose extends BaseEntity {

    @Column(name = "icd_code", length = 20, unique = true)
    @NotBlank(message = "The ICD code of a diagnose cannot be null, empty or blank")
    private String icdCode;

    @Column(name = "diagnose_description", length = 500)
    @NotBlank(message = "The description of a diagnose cannot be null, empty or blank")
    private String diagnoseDescription;

    @ManyToMany(mappedBy = "diagnoseSet")
    private Set<MedicalVisit> medicalVisitSet;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Diagnose diagnose = (Diagnose) o;
        return Objects.equals(icdCode, diagnose.icdCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(icdCode);
    }
}