package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralPractitionerCreateDTO extends DoctorCreateDTO {

    @JsonProperty("practiceNumber")
    @NotBlank(message = "The practice number of a general practitioner cannot be null, empty or blank")
    private String practiceNumber;

    @JsonProperty("practiceAddressId")
    private Long practiceAddressId;
}