package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SickLeaveMapper.class, PatientMapper.class, PersonMapper.class,
    AddressMapper.class, DoctorMapper.class, GeneralPractitionerMapper.class})
public interface MedicalVisitMapper {
    MedicalVisitMapper INSTANCE = Mappers.getMapper(MedicalVisitMapper.class);

    @Mapping(source = "doctor", target = "doctorDTO")
    @Mapping(source = "patient", target = "patientDTO")
    @Mapping(source = "diagnoseSet", target = "diagnoseDTOSet")
    @Mapping(source = "treatmentSet", target = "treatmentDTOSet")
    @Mapping(source = "sickLeave", target = "sickLeaveDTO")
    MedicalVisitDTO toMedicalVisitDTO(MedicalVisit medicalVisit);

    @Mapping(source = "doctorDTO", target = "doctor")
    @Mapping(source = "patientDTO", target = "patient")
    @Mapping(source = "diagnoseDTOSet", target = "diagnoseSet")
    @Mapping(source = "treatmentDTOSet", target = "treatmentSet")
    @Mapping(source = "sickLeaveDTO", target = "sickLeave")
    MedicalVisit toMedicalVisit(MedicalVisitDTO medicalVisitDTO);

    List<MedicalVisitDTO> toMedicalVisitDTOCollection(Collection<MedicalVisit> medicalVisitCollection);

    List<MedicalVisit> toMedicalVisitCollection(Collection<MedicalVisitDTO> medicalVisitDTOCollection);
}