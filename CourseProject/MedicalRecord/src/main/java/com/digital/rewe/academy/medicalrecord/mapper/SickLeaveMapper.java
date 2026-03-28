package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.SickLeave;
import com.digital.rewe.academy.medicalrecord.dto.SickLeaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {MedicalVisitMapper.class})
public interface SickLeaveMapper {
    SickLeaveMapper INSTANCE = Mappers.getMapper(SickLeaveMapper.class);

    @Mapping(source = "medicalVisit.id", target = "medicalVisitId")
    SickLeaveDTO toSickLeaveDTO(SickLeave sickLeave);

    SickLeave toSickLeave(SickLeaveDTO sickLeaveDTO);

    List<SickLeaveDTO> toSickLeaveDTOCollection(Collection<SickLeave> sickLeaveCollection);

    List<SickLeave> toSickLeaveCollection(Collection<SickLeaveDTO> sickLeaveDTOCollection);
}