package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.data.repo.DiagnoseRepository;
import com.digital.rewe.academy.medicalrecord.service.DiagnoseService;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.exception.DiagnoseNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.DiagnoseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiagnoseServiceImpl implements DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;
    private final DiagnoseMapper diagnoseMapper;

    @Override
    public List<DiagnoseDTO> getDiagnoses() {
        return diagnoseMapper.toDiagnoseDTOCollection(diagnoseRepository.findAll());
    }

    @Override
    public List<DiagnoseDTO> getDiagnosesByRank() {
        return diagnoseMapper.toDiagnoseDTOCollection(diagnoseRepository.findDiagnosesWithFrequencyDecreasingOrder());
    }

    @Override
    public DiagnoseDTO getDiagnose(Long id) {
        return diagnoseMapper.toDiagnoseDTO(diagnoseRepository.findById(id)
            .orElseThrow(() -> new DiagnoseNotFoundException("Such a diagnose not found", id)));
    }

    @Override
    public DiagnoseDTO createDiagnose(DiagnoseDTO diagnoseDTO) {
        return diagnoseMapper.toDiagnoseDTO(diagnoseRepository.save(diagnoseMapper.toDiagnose(diagnoseDTO)));
    }

    @Override
    public DiagnoseDTO updateDiagnose(DiagnoseDTO diagnoseDTO, Long id) {
        Optional<Diagnose> optionalDiagnoseToUpdate = diagnoseRepository.findById(id);
        if (optionalDiagnoseToUpdate.isPresent()) {
            Diagnose diagnoseToUpdate = setDiagnoseNonNullFields(diagnoseDTO, optionalDiagnoseToUpdate.get());
            return diagnoseMapper.toDiagnoseDTO(diagnoseRepository.save(diagnoseToUpdate));
        }

        throw new DiagnoseNotFoundException("Such a diagnose not found", id);
    }

    @Override
    public void deleteDiagnose(Long id) {
        this.diagnoseRepository.deleteById(id);
    }

    @Override
    public Diagnose setDiagnoseNonNullFields(DiagnoseDTO diagnoseDTO, Diagnose diagnoseToUpdate) {
        if (diagnoseDTO.getIcdCode() != null) {
            diagnoseToUpdate.setIcdCode(diagnoseDTO.getIcdCode());
        }

        if (diagnoseDTO.getDiagnoseDescription() != null) {
            diagnoseToUpdate.setDiagnoseDescription(diagnoseDTO.getDiagnoseDescription());
        }

        return diagnoseToUpdate;
    }
}