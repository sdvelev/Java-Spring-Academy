package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SickLeaveDTO extends BaseEntityDTO {

    @JsonProperty("startDate")
    @NotNull(message = "The start date of a sick leave cannot be null")
    private LocalDate startDate;

    @JsonProperty("endDate")
    @NotNull(message = "The end date of a sick leave cannot be null")
    private LocalDate endDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("medicalVisitId")
    private Long medicalVisitId;
}