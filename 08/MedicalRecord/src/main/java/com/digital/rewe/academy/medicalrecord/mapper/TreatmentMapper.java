package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {
    TreatmentMapper INSTANCE = Mappers.getMapper(TreatmentMapper.class);

    TreatmentDTO toTreatmentDTO(Treatment treatment);

    Treatment toTreatment(TreatmentDTO treatmentDTO);

    List<TreatmentDTO> toTreatmentDTOCollection(Collection<Treatment> treatmentCollection);

    List<Treatment> toTreatmentCollection(Collection<TreatmentDTO> treatmentDTOCollection);
}