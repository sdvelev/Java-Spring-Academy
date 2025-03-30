package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import jakarta.websocket.server.PathParam;
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
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @GetMapping("/most-sick-leaves")
    public List<DoctorDTO> getDoctorWithMostSickLeaves() {
        return doctorService.getDoctorWithMostSickLeaves();
    }

    @PostMapping()
    public DoctorDTO createDoctor(@RequestBody DoctorDTO doctorDTO) {
        return doctorService.createDoctor(doctorDTO);
    }

    @PostMapping(params = {"address-id"})
    public DoctorDTO createDoctorWithExistingAddress(@RequestBody DoctorDTO doctorDTO, @RequestParam("address-id") Long addressId) {
        return doctorService.createDoctorWithExistingAddress(doctorDTO, addressId);
    }

    @PutMapping("/{id}")
    public DoctorDTO updateDoctor(@RequestBody DoctorDTO doctorDTO, @PathVariable Long id) {
        return this.doctorService.updateDoctor(doctorDTO, id);
    }

    @PatchMapping(params = {"add-specialty"}, path="/{doctorId}")
    public DoctorDTO addSpecialty(@PathVariable Long doctorId, @RequestParam("add-specialty") Long specialtyId) {
        return this.doctorService.addSpecialty(doctorId, specialtyId);
    }

    @PatchMapping(params = {"remove-specialty"}, path="/{doctorId}")
    public DoctorDTO removeSpecialty(@PathVariable Long doctorId, @RequestParam("remove-specialty") Long specialtyId) {
        return this.doctorService.removeSpecialty(doctorId, specialtyId);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        this.doctorService.deleteDoctor(id);
    }
}