package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public GeneralPractitionerDTO getGeneralPractitioner(@PathVariable Long id) {
        return generalPractitionerService.getGeneralPractitioner(id);
    }

    @PostMapping()
    public GeneralPractitionerDTO createGeneralPractitioner(@RequestBody GeneralPractitionerDTO generalPractitionerDTO) {
        return generalPractitionerService.createGeneralPractitioner(generalPractitionerDTO);
    }

    @PostMapping(params = {"permanent-address-id", "practice-address-id"})
    public GeneralPractitionerDTO createGeneralPractitionerWithExistingAddress(@RequestBody GeneralPractitionerDTO generalPractitionerDTO,
                                                                               @RequestParam("permanent-address-id") Long permanentAddressId,
                                                                               @RequestParam("practice-address-id") Long practiceAddressId) {
        return generalPractitionerService.createGeneralPractitionerWithExistingAddresses(generalPractitionerDTO, permanentAddressId, practiceAddressId);
    }

    @PutMapping("/{id}")
    public DoctorDTO updateGeneralPractitioner(@RequestBody GeneralPractitionerDTO generalPractitionerDTO, @PathVariable Long id) {
        return this.generalPractitionerService.updateGeneralPractitioner(generalPractitionerDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteGeneralPractitioner(@PathVariable Long id) {
        this.generalPractitionerService.deleteGeneralPractitioner(id);
    }
}