package com.digital.rewe.academy.medicalrecord.dto;

import com.digital.rewe.academy.medicalrecord.data.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalVisitCreateDTO extends BaseEntity {

    @JsonProperty("dateOfVisit")
    @NotNull(message = "The date of a medical visit cannot be null")
    @PastOrPresent(message = "The date of a medical visit cannot be in the future")
    private LocalDate dateOfVisit;

    @JsonProperty("doctorId")
    @NotNull(message = "The provided doctor id cannot be null")
    @Positive(message = "The provided doctor id must be positive")
    private Long doctorId;

    @JsonProperty("patientId")
    @NotNull(message = "The provided patient id cannot be null")
    @Positive(message = "The provided patient id must be positive")
    private Long patientId;
}