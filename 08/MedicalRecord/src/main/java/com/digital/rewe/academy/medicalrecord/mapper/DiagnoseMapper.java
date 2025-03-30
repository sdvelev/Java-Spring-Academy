package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DiagnoseMapper {
    DiagnoseMapper INSTANCE = Mappers.getMapper(DiagnoseMapper.class);

    DiagnoseDTO toDiagnoseDTO(Diagnose diagnose);

    Diagnose toDiagnose(DiagnoseDTO diagnoseDTO);

    List<DiagnoseDTO> toDiagnoseDTOCollection(Collection<Diagnose> diagnoseCollection);

    List<Diagnose> toDiagnoseCollection(Collection<DiagnoseDTO> diagnoseDTOCollection);
}