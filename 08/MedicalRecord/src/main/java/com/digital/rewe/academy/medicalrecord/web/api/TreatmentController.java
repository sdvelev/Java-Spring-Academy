package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.TreatmentService;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
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
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @GetMapping
    public List<TreatmentDTO> getTreatments() {
        return treatmentService.getTreatments();
    }

    @GetMapping("/{id}")
    public TreatmentDTO getTreatment(@PathVariable @Valid @Positive Long id) {
        return treatmentService.getTreatment(id);
    }

    @PostMapping
    public TreatmentDTO createTreatment(@RequestBody @Valid TreatmentDTO treatmentDTO) {
        return treatmentService.createTreatment(treatmentDTO);
    }

    @PutMapping("/{id}")
    public TreatmentDTO updateTreatment(@RequestBody @Valid TreatmentDTO treatmentDTO,
                                        @PathVariable @Valid @Positive Long id) {
        return this.treatmentService.updateTreatment(treatmentDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTreatment(@PathVariable @Valid Long id) {
        this.treatmentService.deleteTreatment(id);
    }
}