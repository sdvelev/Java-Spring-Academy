package com.digital.rewe.academy.medicalrecord.dto;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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