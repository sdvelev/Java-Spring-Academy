package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.PatientService;
import com.digital.rewe.academy.medicalrecord.dto.PatientCountPerGeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    @GetMapping("/diagnose/{diagnoseId}")
    public List<PatientDTO> getPatientsWithSpecificDiagnose(@PathVariable @Valid @Positive Long diagnoseId) {
        return patientService.getPatientsWithSpecificDiagnose(diagnoseId);
    }

    @GetMapping("/general-practitioner/{generalPractitionerId}")
    public List<PatientDTO> getPatientsWithGeneralPractitioner(@PathVariable @Valid @Positive Long generalPractitionerId) {
        return patientService.getPatientsWithGeneralPractitioner(generalPractitionerId);
    }

    @GetMapping("/count/general-practitioner/{generalPractitionerId}")
    public Long getCountByGeneralPractitionerId(@PathVariable @Valid @Positive Long generalPractitionerId) {
        return patientService.getCountByGeneralPractitionerId(generalPractitionerId);
    }

    @GetMapping("/count/general-practitioner")
    public List<PatientCountPerGeneralPractitionerDTO> getCountPerGeneralPractitioner() {
        return patientService.getCountPerGeneralPractitioner();
    }

    @GetMapping("/{id}")
    public PatientDTO getPatient(@PathVariable @Valid @Positive Long id) {
        return patientService.getPatient(id);
    }

    @PostMapping()
    public PatientDTO createPatient(@RequestBody @Valid PatientDTO patientDTO) {
        return patientService.createPatient(patientDTO);
    }

    @PostMapping(params = {"address-id"})
    public PatientDTO createPatientWithExistingAddress(@RequestBody @Valid PatientDTO patientDTO,
                                                       @RequestParam("address-id") @Valid @Positive Long addressId) {
        return patientService.createPatientWithExistingAddress(patientDTO, addressId);
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatient(@RequestBody @Valid PatientDTO patientDTO, @PathVariable @Valid @Positive Long id) {
        return this.patientService.updatePatient(patientDTO, id);
    }

    @PatchMapping(params = {"add-general-practitioner"}, path="/{patientId}")
    public PatientDTO addGeneralPractitioner(@PathVariable @Valid @Positive Long patientId,
                                             @RequestParam("add-general-practitioner") @Valid @Positive Long generalPractitionerId) {
        return this.patientService.addGeneralPractitioner(patientId, generalPractitionerId);
    }

    @PatchMapping(params = {"remove-general-practitioner"}, path="/{patientId}")
    public PatientDTO removeGeneralPractitioner(@PathVariable @Valid @Positive Long patientId,
                                                @RequestParam(value = "remove-specialty", required = false) @Valid Long generalPractitionerId) {
        return this.patientService.removeGeneralPractitioner(patientId);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable @Valid @Positive Long id) {
        this.patientService.deletePatient(id);
    }
}