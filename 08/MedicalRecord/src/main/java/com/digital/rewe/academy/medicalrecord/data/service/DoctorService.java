package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getDoctors();
    DoctorDTO getDoctor(Long id);
//    DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO);
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    DoctorDTO createDoctorWithExistingAddress(DoctorDTO doctorDTO, Long addressId);
    DoctorDTO updateDoctor(DoctorDTO doctorDTO, Long id);
    void deleteDoctor(Long id);
    Doctor setDoctorNonNullFields(DoctorDTO doctorDTO, Doctor doctorToUpdate);

    DoctorDTO addSpecialty(Long  doctorId, Long specialtyId);
    DoctorDTO removeSpecialty(Long  doctorId, Long specialtyId);


}
