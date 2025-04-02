package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Specialty;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDTO> getSpecialties();

    SpecialtyDTO getSpecialty(Long id);

    SpecialtyDTO createSpecialty(SpecialtyDTO specialtyDTO);

    SpecialtyDTO updateSpecialty(SpecialtyDTO specialtyDTO, Long id);

    Specialty setSpecialtyNonNullFields(SpecialtyDTO specialtyDTO, Specialty specialtyToUpdate);

    void deleteSpecialty(Long id);
}
