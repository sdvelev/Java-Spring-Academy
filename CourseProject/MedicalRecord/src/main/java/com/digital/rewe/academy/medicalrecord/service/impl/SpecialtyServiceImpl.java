package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Specialty;
import com.digital.rewe.academy.medicalrecord.data.repo.SpecialtyRepository;
import com.digital.rewe.academy.medicalrecord.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
import com.digital.rewe.academy.medicalrecord.exception.SpecialtyNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.SpecialtyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public List<SpecialtyDTO> getSpecialties() {
        return specialtyMapper.toSpecialtyDTOCollection(specialtyRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public SpecialtyDTO getSpecialty(Long id) {
        return specialtyMapper.toSpecialtyDTO(specialtyRepository.findById(id)
            .orElseThrow(() -> new SpecialtyNotFoundException("Such a specialty not found", id)));
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public SpecialtyDTO createSpecialty(SpecialtyDTO specialtyDTO) {
        return specialtyMapper.toSpecialtyDTO(specialtyRepository.save(specialtyMapper.toSpecialty(specialtyDTO)));
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public SpecialtyDTO updateSpecialty(SpecialtyDTO specialtyDTO, Long id) {
        Optional<Specialty> optionalSpecialtyToUpdate = specialtyRepository.findById(id);
        if (optionalSpecialtyToUpdate.isPresent()) {
            Specialty specialtyToUpdate = setSpecialtyNonNullFields(specialtyDTO, optionalSpecialtyToUpdate.get());
            return specialtyMapper.toSpecialtyDTO(specialtyRepository.save(specialtyToUpdate));
        }

        throw new SpecialtyNotFoundException("Such specialty not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSpecialty(Long id) {
        this.specialtyRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public Specialty setSpecialtyNonNullFields(SpecialtyDTO specialtyDTO, Specialty specialtyToUpdate) {
        if (specialtyDTO.getSpecialtyName() != null) {
            specialtyToUpdate.setSpecialtyName(specialtyDTO.getSpecialtyName());
        }

        if (specialtyDTO.getDescription() != null) {
            specialtyToUpdate.setDescription(specialtyDTO.getDescription());
        }

        return specialtyToUpdate;
    }
}