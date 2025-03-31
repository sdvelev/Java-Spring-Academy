package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
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
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public List<SpecialtyDTO> getSpecialties() {
        return specialtyService.getSpecialties();
    }

    @GetMapping("/{id}")
    public SpecialtyDTO getSpecialty(@PathVariable @Positive Long id) {
        return specialtyService.getSpecialty(id);
    }

    @PostMapping
    public SpecialtyDTO createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        return specialtyService.createSpecialty(specialtyDTO);
    }

    @PutMapping("/{id}")
    public SpecialtyDTO updateSpecialty(@RequestBody SpecialtyDTO specialtyDTO, @PathVariable @Positive Long id) {
        return this.specialtyService.updateSpecialty(specialtyDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteSpecialty(@PathVariable @Positive Long id) {
        this.specialtyService.deleteSpecialty(id);
    }
}