package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerCreateDTO;
import com.digital.rewe.academy.medicalrecord.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general-practitioners")
public class GeneralPractitionerController {

    private final GeneralPractitionerService generalPractitionerService;

    @GetMapping
    public List<GeneralPractitionerDTO> getGeneralPractitioners() {
        return generalPractitionerService.getGeneralPractitioners();
    }

    @GetMapping("/{id}")
    public GeneralPractitionerDTO getGeneralPractitioner(@PathVariable @Valid Long id) {
        return generalPractitionerService.getGeneralPractitioner(id);
    }

    @PostMapping()
    public GeneralPractitionerDTO createGeneralPractitioner(@RequestBody @Valid
                                                            GeneralPractitionerCreateDTO generalPractitionerCreateDTO) {
        return generalPractitionerService.createGeneralPractitioner(generalPractitionerCreateDTO);
    }

    @PutMapping("/{id}")
    public DoctorDTO updateGeneralPractitioner(@RequestBody GeneralPractitionerDTO generalPractitionerDTO, @PathVariable @Valid @Positive Long id) {
        return this.generalPractitionerService.updateGeneralPractitioner(generalPractitionerDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteGeneralPractitioner(@PathVariable @Valid @Positive Long id) {
        this.generalPractitionerService.deleteGeneralPractitioner(id);
    }
}