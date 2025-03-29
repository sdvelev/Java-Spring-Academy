package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Specialty;
import com.digital.rewe.academy.medicalrecord.data.repo.SpecialtyRepository;
import com.digital.rewe.academy.medicalrecord.data.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
import com.digital.rewe.academy.medicalrecord.mapper.SpecialtyMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public List<SpecialtyDTO> getSpecialties() {
        return specialtyMapper.toSpecialtyDTOCollection(specialtyRepository.findAll());
    }

    @Override
    public SpecialtyDTO getSpecialty(Long id) {
        return specialtyMapper.toSpecialtyDTO(specialtyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Specialty with id: " + id + " not found")));
    }

    @Override
    public SpecialtyDTO createSpecialty(SpecialtyDTO specialtyDTO) {
        return specialtyMapper.toSpecialtyDTO(specialtyRepository.save(specialtyMapper.toSpecialty(specialtyDTO)));
    }

    @Override
    public SpecialtyDTO updateSpecialty(SpecialtyDTO specialtyDTO, Long id) {
        Optional<Specialty> optionalSpecialtyToUpdate = specialtyRepository.findById(id);
        if (optionalSpecialtyToUpdate.isPresent()) {
            Specialty specialtyToUpdate = setSpecialtyNonNullFields(specialtyDTO, optionalSpecialtyToUpdate.get());
            return specialtyMapper.toSpecialtyDTO(specialtyRepository.save(specialtyToUpdate));
        }

        throw new EntityNotFoundException("Specialty with id " + id + " not found");
    }

    @Override
    public void deleteSpecialty(Long id) {
        this.specialtyRepository.deleteById(id);
    }

    @Override
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