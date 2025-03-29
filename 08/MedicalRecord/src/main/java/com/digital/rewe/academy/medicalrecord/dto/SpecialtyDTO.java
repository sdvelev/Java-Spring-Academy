package com.digital.rewe.academy.medicalrecord.dto;

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
public class SpecialtyDTO extends BaseEntityDTO {

    @JsonProperty("specialtyName")
    @NotBlank(message = "The name of a specialty cannot be null, empty or blank")
    private String specialtyName;

    @JsonProperty("description")
    private String description;
}