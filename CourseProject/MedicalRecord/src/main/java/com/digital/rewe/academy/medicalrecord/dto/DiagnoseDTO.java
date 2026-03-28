package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseDTO extends BaseEntityDTO {

    @JsonProperty("icdCode")
    @NotBlank(message = "The ICD code of a diagnose cannot be null, empty or blank")
    private String icdCode;

    @JsonProperty("diagnoseDescription")
    @NotBlank(message = "The description of a diagnose cannot be null, empty or blank")
    private String diagnoseDescription;
}