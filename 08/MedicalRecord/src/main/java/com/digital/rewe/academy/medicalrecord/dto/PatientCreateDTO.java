package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreateDTO extends PersonCreateDTO {

    @JsonProperty("lastPaidHealthInsurance")
    private LocalDate lastPaidHealthInsurance;
}