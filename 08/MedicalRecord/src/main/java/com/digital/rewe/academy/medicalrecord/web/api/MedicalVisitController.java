package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.MedicalVisitService;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medical-visits")
public class MedicalVisitController {

    private final MedicalVisitService medicalVisitService;

    @GetMapping
    public List<MedicalVisitDTO> getMedicalVisits() {
        return medicalVisitService.getMedicalVisits();
    }

    @GetMapping("/{id}")
    public MedicalVisitDTO getMedicalVisit(@PathVariable Long id) {
        return medicalVisitService.getMedicalVisit(id);
    }

    @GetMapping("/count/doctor/{doctorId}")
    public Long getMedicalVisitsCountByDoctorId(@PathVariable Long doctorId) {
        return medicalVisitService.getMedicalVisitsCountByDoctorId(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalVisitDTO> getMedicalVisitsByPatientId(@PathVariable Long patientId) {
        return medicalVisitService.getMedicalVisitsByPatientId(patientId);
    }

    @GetMapping(path = "/period", params = {"start-date", "end-date"})
    public List<MedicalVisitDTO> getMedicalVisitsByPeriod(@RequestParam("start-date") LocalDate startDate,
                                                          @RequestParam("end-date") LocalDate endDate) {
        return medicalVisitService.getMedicalVisitsByPeriod(startDate, endDate);
    }

    @GetMapping(path = "/doctor/{doctorId}/period", params = {"start-date", "end-date"})
    public List<MedicalVisitDTO> getMedicalVisitsByPeriodAndDoctorId(@PathVariable Long doctorId,
                                                                     @RequestParam("start-date") LocalDate startDate,
                                                                     @RequestParam("end-date") LocalDate endDate) {
        return medicalVisitService.getMedicalVisitsByPeriodAndDoctorId(doctorId, startDate, endDate);
    }

    @PostMapping()
    public MedicalVisitDTO createMedicalVisit(@RequestBody MedicalVisitCreateDTO medicalVisitCreateDTO) {
        return medicalVisitService.createMedicalVisit(medicalVisitCreateDTO);
    }

    @PutMapping("/{id}")
    public MedicalVisitDTO updateMedicalVisit(@RequestBody MedicalVisitDTO medicalVisitDTO, @PathVariable Long id) {
        return this.medicalVisitService.updateMedicalVisit(medicalVisitDTO, id);
    }

    @PatchMapping(params = {"add-diagnose"}, path="/{doctorId}")
    public MedicalVisitDTO addDiagnose(@PathVariable Long doctorId, @RequestParam("add-diagnose") Long diagnoseId) {
        return this.medicalVisitService.addDiagnose(doctorId, diagnoseId);
    }

    @PatchMapping(params = {"remove-diagnose"}, path="/{doctorId}")
    public MedicalVisitDTO removeDiagnose(@PathVariable Long doctorId, @RequestParam("remove-diagnose") Long diagnoseId) {
        return this.medicalVisitService.removeDiagnose(doctorId, diagnoseId);
    }

    @PatchMapping(params = {"add-treatment"}, path="/{doctorId}")
    public MedicalVisitDTO addTreatment(@PathVariable Long doctorId, @RequestParam("add-treatment") Long treatmentId) {
        return this.medicalVisitService.addTreatment(doctorId, treatmentId);
    }

    @PatchMapping(params = {"remove-treatment"}, path="/{doctorId}")
    public MedicalVisitDTO removeTreatment(@PathVariable Long doctorId, @RequestParam("remove-treatment") Long treatmentId) {
        return this.medicalVisitService.removeTreatment(doctorId, treatmentId);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicalVisit(@PathVariable Long id) {
        this.medicalVisitService.deleteMedicalVisit(id);
    }
}