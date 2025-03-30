package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.PatientService;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientDTO> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{id}")
    public PatientDTO getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @PostMapping()
    public PatientDTO createPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.createPatient(patientDTO);
    }

    @PostMapping(params = {"address-id"})
    public PatientDTO createPatientWithExistingAddress(@RequestBody PatientDTO patientDTO, @RequestParam("address-id") Long addressId) {
        return patientService.createPatientWithExistingAddress(patientDTO, addressId);
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatient(@RequestBody PatientDTO patientDTO, @PathVariable Long id) {
        return this.patientService.updatePatient(patientDTO, id);
    }

    @PatchMapping(params = {"add-general-practitioner"}, path="/{patientId}")
    public PatientDTO addGeneralPractitioner(@PathVariable Long patientId, @RequestParam("add-general-practitioner") Long generalPractitionerId) {
        return this.patientService.addGeneralPractitioner(patientId, generalPractitionerId);
    }

    @PatchMapping(params = {"remove-general-practitioner"}, path="/{patientId}")
    public PatientDTO removeGeneralPractitioner(@PathVariable Long patientId, @RequestParam(value = "remove-specialty", required = false) Long generalPractitionerId) {
        return this.patientService.removeGeneralPractitioner(patientId);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        this.patientService.deletePatient(id);
    }
}