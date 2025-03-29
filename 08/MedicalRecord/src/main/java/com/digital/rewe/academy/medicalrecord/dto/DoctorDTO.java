package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO extends PersonDTO {

    @JsonProperty("uniqueIdNumber")
    @NotBlank(message = "The unique ID number of a doctor cannot be null, empty or blank")
    private String uniqueIdNumber;

    @JsonProperty("specialtySet")
    private Set<SpecialtyDTO> specialtyDTOSet;
}