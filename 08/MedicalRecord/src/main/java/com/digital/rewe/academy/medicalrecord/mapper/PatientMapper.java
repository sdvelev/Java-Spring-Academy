package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.dto.PatientCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {GeneralPractitionerMapper.class, DoctorMapper.class,
                SpecialtyMapper.class, PersonMapper.class, AddressMapper.class, PasswordEncoderMapper.class})
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    @Mapping(source = "generalPractitioner", target = "generalPractitionerDTO")
    @Mapping(source = "permanentAddress", target = "permanentAddressDTO")
    PatientDTO toPatientDTO(Patient patient);

    @Mapping(source = "generalPractitionerDTO", target = "generalPractitioner")
    @Mapping(source = "permanentAddressDTO", target = "permanentAddress")
    Patient toPatient(PatientDTO patientDTO);

    @Mapping(source = "unencryptedPassword", target = "password", qualifiedBy = EncodedMapping.class)
    PatientDTO toPatientDTO(PatientCreateDTO patientCreateDTO);

    List<PatientDTO> toPatientDTOCollection(Collection<Patient> patientCollection);

    List<Patient> toPatientCollection(Collection<PatientDTO> patientDTOCollection);
}