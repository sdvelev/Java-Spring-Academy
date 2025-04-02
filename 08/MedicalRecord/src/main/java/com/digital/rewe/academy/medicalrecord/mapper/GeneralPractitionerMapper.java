package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PersonMapper.class, AddressMapper.class, PasswordEncoderMapper.class})
public interface GeneralPractitionerMapper {
    GeneralPractitionerMapper INSTANCE = Mappers.getMapper(GeneralPractitionerMapper.class);

    @Mapping(source = "practiceAddress", target = "practiceAddressDTO")
    @Mapping(source = "permanentAddress", target = "permanentAddressDTO")
    GeneralPractitionerDTO toGeneralPractitionerDTO(GeneralPractitioner generalPractitioner);

    @Mapping(source = "practiceAddressDTO", target = "practiceAddress")
    @Mapping(source = "permanentAddressDTO", target = "permanentAddress")
    GeneralPractitioner toGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO);

    @Mapping(source = "unencryptedPassword", target = "password", qualifiedBy = EncodedMapping.class)
    GeneralPractitionerDTO toGeneralPractitionerDTO(GeneralPractitionerCreateDTO generalPractitionerCreateDTO);

    List<GeneralPractitionerDTO> toGeneralPractitionerDTOCollection(Collection<GeneralPractitioner> generalPractitionerCollection);

    List<GeneralPractitioner> toGeneralPractitionerCollection(Collection<GeneralPractitionerDTO> generalPractitionerDTOCollection);
}