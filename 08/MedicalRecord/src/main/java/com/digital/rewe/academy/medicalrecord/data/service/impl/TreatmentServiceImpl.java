package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import com.digital.rewe.academy.medicalrecord.data.repo.TreatmentRepository;
import com.digital.rewe.academy.medicalrecord.data.service.TreatmentService;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
import com.digital.rewe.academy.medicalrecord.mapper.TreatmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatmentMapper treatmentMapper;

    @Override
    public List<TreatmentDTO> getTreatments() {
        return treatmentMapper.toTreatmentDTOCollection(treatmentRepository.findAll());
    }

    @Override
    public TreatmentDTO getTreatment(Long id) {
        return treatmentMapper.toTreatmentDTO(treatmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Treatment with id: " + id + " not found")));
    }

    @Override
    public TreatmentDTO createTreatment(TreatmentDTO treatmentDTO) {
        return treatmentMapper.toTreatmentDTO(treatmentRepository.save(treatmentMapper.toTreatment(treatmentDTO)));
    }

    @Override
    public TreatmentDTO updateTreatment(TreatmentDTO treatmentDTO, Long id) {
        Optional<Treatment> optionalTreatmentToUpdate = treatmentRepository.findById(id);
        if (optionalTreatmentToUpdate.isPresent()) {
            Treatment treatmentToUpdate = setTreatmentNonNullFields(treatmentDTO, optionalTreatmentToUpdate.get());
            return treatmentMapper.toTreatmentDTO(treatmentRepository.save(treatmentToUpdate));
        }

        throw new EntityNotFoundException("Treatment with id " + id + " not found");
    }

    @Override
    public void deleteTreatment(Long id) {
        this.treatmentRepository.deleteById(id);
    }

    @Override
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