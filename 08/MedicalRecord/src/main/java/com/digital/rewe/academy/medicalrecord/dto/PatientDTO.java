package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO extends PersonDTO {

    @JsonProperty("lastPaidHealthInsurance")
    private LocalDate lastPaidHealthInsurance;

    @JsonProperty("generalPractitioner")
    private GeneralPractitionerDTO generalPractitioner;
}