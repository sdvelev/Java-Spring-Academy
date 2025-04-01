package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.SickLeaveService;
import com.digital.rewe.academy.medicalrecord.dto.SickLeaveDTO;
import com.digital.rewe.academy.medicalrecord.util.MonthCountProjection;
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
@RequestMapping("/api/sick-leaves")
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;

    @GetMapping
    public List<SickLeaveDTO> getSickLeaves() {
        return sickLeaveService.getSickLeaves();
    }

    @GetMapping("/{id}")
    public SickLeaveDTO getSickLeave(@PathVariable @Valid @Positive Long id) {
        return sickLeaveService.getSickLeave(id);
    }

    @GetMapping("/months-count-history-statistic")
    public List<MonthCountProjection> getMonthWithHighestNumberOfSickLeaves() {
        return sickLeaveService.getMonthWithHighestNumberOfSickLeaves();
    }

    @PostMapping
    public SickLeaveDTO createSickLeave(@RequestBody @Valid SickLeaveDTO sickLeaveDTO) {
        return sickLeaveService.createSickLeave(sickLeaveDTO);
    }

    @PutMapping("/{id}")
    public SickLeaveDTO updateSickLeave(@RequestBody @Valid SickLeaveDTO sickLeaveDTO,
                                        @PathVariable @Valid @Positive Long id) {
        return this.sickLeaveService.updateSickLeave(sickLeaveDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteSickLeave(@PathVariable @Valid @Positive Long id) {
        this.sickLeaveService.deleteSickLeave(id);
    }
}