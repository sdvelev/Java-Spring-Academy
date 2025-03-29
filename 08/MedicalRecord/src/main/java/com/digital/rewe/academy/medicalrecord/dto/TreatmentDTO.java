package com.digital.rewe.academy.medicalrecord.dto;

import com.digital.rewe.academy.medicalrecord.data.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentDTO extends BaseEntityDTO {

    @JsonProperty("name")
    @NotBlank(message = "The name of a treatment cannot be null, empty or blank")
    private String name;

    @JsonProperty("description")
    private String description;
}