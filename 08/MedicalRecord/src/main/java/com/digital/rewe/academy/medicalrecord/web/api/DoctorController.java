package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
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
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDTO> getDoctors() {
        return doctorService.getDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDTO getDoctor(@PathVariable @Valid @Positive Long id) {
        return doctorService.getDoctor(id);
    }

    @GetMapping("/most-sick-leaves")
    public List<DoctorDTO> getDoctorWithMostSickLeaves() {
        return doctorService.getDoctorWithMostSickLeaves();
    }

    @PostMapping()
    public DoctorDTO createDoctor(@RequestBody @Valid DoctorCreateDTO doctorCreateDTO) {
        return doctorService.createDoctor(doctorCreateDTO);
    }

//    @PostMapping(params = {"address-id"})
//    public DoctorDTO createDoctorWithExistingAddress(@RequestBody @Valid DoctorDTO doctorDTO,
//                                                     @RequestParam("address-id") @Valid @Positive Long addressId) {
//        return doctorService.createDoctorWithExistingAddress(doctorDTO, addressId);
//    }

    @PutMapping("/{id}")
    public DoctorDTO updateDoctor(@RequestBody @Valid DoctorDTO doctorDTO,
                                  @PathVariable @Valid @Positive Long id) {
        return this.doctorService.updateDoctor(doctorDTO, id);
    }

    @PatchMapping(params = {"add-specialty"}, path="/{doctorId}")
    public DoctorDTO addSpecialty(@PathVariable @Valid @Positive Long doctorId,
                                  @RequestParam("add-specialty") @Valid @Positive Long specialtyId) {
        return this.doctorService.addSpecialty(doctorId, specialtyId);
    }

    @PatchMapping(params = {"remove-specialty"}, path="/{doctorId}")
    public DoctorDTO removeSpecialty(@PathVariable @Valid @Positive Long doctorId,
                                     @RequestParam("remove-specialty") @Valid @Positive Long specialtyId) {
        return this.doctorService.removeSpecialty(doctorId, specialtyId);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable @Valid @Positive Long id) {
        this.doctorService.deleteDoctor(id);
    }
}