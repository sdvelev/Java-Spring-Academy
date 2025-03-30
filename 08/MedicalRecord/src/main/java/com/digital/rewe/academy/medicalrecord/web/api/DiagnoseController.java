package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.DiagnoseService;
import com.digital.rewe.academy.medicalrecord.data.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
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
@RequestMapping("/api/diagnoses")
public class DiagnoseController {

    private final DiagnoseService diagnoseService;

    @GetMapping
    public List<DiagnoseDTO> getDiagnoses() {
        return diagnoseService.getDiagnoses();
    }

    @GetMapping("/rank")
    public List<DiagnoseDTO> getDiagnosesByRank() {
        return diagnoseService.getDiagnosesByRank();
    }

    @GetMapping("/{id}")
    public DiagnoseDTO getDiagnose(@PathVariable Long id) {
        return diagnoseService.getDiagnose(id);
    }

    @PostMapping
    public DiagnoseDTO createDiagnose(@RequestBody DiagnoseDTO diagnoseDTO) {
        return diagnoseService.createDiagnose(diagnoseDTO);
    }

    @PutMapping("/{id}")
    public DiagnoseDTO updateDiagnose(@RequestBody DiagnoseDTO diagnoseDTO, @PathVariable Long id) {
        return this.diagnoseService.updateDiagnose(diagnoseDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteDiagnose(@PathVariable Long id) {
        this.diagnoseService.deleteDiagnose(id);
    }
}