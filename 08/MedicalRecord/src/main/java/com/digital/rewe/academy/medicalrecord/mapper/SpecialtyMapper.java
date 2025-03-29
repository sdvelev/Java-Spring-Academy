package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Specialty;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    SpecialtyMapper INSTANCE = Mappers.getMapper(SpecialtyMapper.class);

    SpecialtyDTO toSpecialtyDTO(Specialty specialty);

    Specialty toSpecialty(SpecialtyDTO specialtyDTO);

    List<SpecialtyDTO> toSpecialtyDTOCollection(Collection<Specialty> specialtyCollection);

    List<Specialty> toSpecialtyCollection(Collection<SpecialtyDTO> specialtyDTOCollection);
}