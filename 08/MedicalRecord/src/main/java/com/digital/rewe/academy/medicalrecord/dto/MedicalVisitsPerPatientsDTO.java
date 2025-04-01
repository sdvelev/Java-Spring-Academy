package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalVisitsPerPatientsDTO {

    @JsonProperty("patientId")
    private Long patientId;

    @JsonProperty("medicalVisitId")
    private Long medicalVisitId;
}