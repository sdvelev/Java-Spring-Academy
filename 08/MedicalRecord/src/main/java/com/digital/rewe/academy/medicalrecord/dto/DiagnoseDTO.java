package com.digital.rewe.academy.medicalrecord.dto;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseDTO extends BaseEntityDTO {

    @JsonProperty("icdCode")
    @NotBlank(message = "The ICD code of a diagnose cannot be null, empty or blank")
    private String icdCode;

    @JsonProperty("diagnoseDescription")
    @NotBlank(message = "The description of a diagnose cannot be null, empty or blank")
    private String diagnoseDescription;
}