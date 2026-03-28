package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import com.digital.rewe.academy.medicalrecord.data.repo.TreatmentRepository;
import com.digital.rewe.academy.medicalrecord.service.TreatmentService;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
import com.digital.rewe.academy.medicalrecord.exception.TreatmentNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.TreatmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatmentMapper treatmentMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public List<TreatmentDTO> getTreatments() {
        return treatmentMapper.toTreatmentDTOCollection(treatmentRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public TreatmentDTO getTreatment(Long id) {
        return treatmentMapper.toTreatmentDTO(treatmentRepository.findById(id)
            .orElseThrow(() -> new TreatmentNotFoundException("Such a treatment not found", id)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public TreatmentDTO createTreatment(TreatmentDTO treatmentDTO) {
        return treatmentMapper.toTreatmentDTO(treatmentRepository.save(treatmentMapper.toTreatment(treatmentDTO)));
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public TreatmentDTO updateTreatment(TreatmentDTO treatmentDTO, Long id) {
        Optional<Treatment> optionalTreatmentToUpdate = treatmentRepository.findById(id);
        if (optionalTreatmentToUpdate.isPresent()) {
            Treatment treatmentToUpdate = setTreatmentNonNullFields(treatmentDTO, optionalTreatmentToUpdate.get());
            return treatmentMapper.toTreatmentDTO(treatmentRepository.save(treatmentToUpdate));
        }

        throw new TreatmentNotFoundException("Such a treatment not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteTreatment(Long id) {
        this.treatmentRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public Treatment setTreatmentNonNullFields(TreatmentDTO treatmentDTO, Treatment treatmentToUpdate) {
        if (treatmentDTO.getName() != null) {
            treatmentToUpdate.setName(treatmentDTO.getName());
        }

        if (treatmentDTO.getDescription() != null) {
            treatmentToUpdate.setDescription(treatmentDTO.getDescription());
        }

        return treatmentToUpdate;
    }
}