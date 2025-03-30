package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralPractitionerDTO extends DoctorDTO {

    @JsonProperty("practiceNumber")
    @NotBlank(message = "The practice number of a general practitioner cannot be null, empty or blank")
    private String practiceNumber;

    @JsonProperty("practiceAddressDTO")
    private AddressDTO practiceAddressDTO;
}