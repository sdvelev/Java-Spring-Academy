package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;

import java.util.List;

public interface DoctorService {

    List<DoctorDTO> getDoctors();

    DoctorDTO getDoctor(Long id);

    List<DoctorDTO> getDoctorWithMostSickLeaves();

    DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO);

    DoctorDTO updateDoctor(DoctorDTO doctorDTO, Long id);

    Doctor setDoctorNonNullFields(DoctorDTO doctorDTO, Doctor doctorToUpdate);

    void deleteDoctor(Long id);

    DoctorDTO addSpecialty(Long doctorId, Long specialtyId);

    DoctorDTO removeSpecialty(Long doctorId, Long specialtyId);
}