package com.digital.rewe.academy.medicalrecord.dto;

import com.digital.rewe.academy.medicalrecord.data.entity.BaseEntity;
import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalVisitDTO extends BaseEntity {

    @JsonProperty("dateOfVisit")
    @NotNull(message = "The date of a medical visit cannot be null")
    @PastOrPresent(message = "The date of a medical visit cannot be in the future")
    private LocalDate dateOfVisit;

    @JsonProperty("doctorId")
    private Long doctorId;

    @JsonProperty("patientId")
    private Long patientId;

    @JsonProperty("diagnoseDTOSet")
    private Set<DiagnoseDTO> diagnoseDTOSet;

    @JsonProperty("treatmentDTOSet")
    private Set<Treatment> treatmentSet;

    @JsonProperty("sickLeave")
    private SickLeaveDTO sickLeaveDTO;
}