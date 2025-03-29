package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class, PersonMapper.class, AddressMapper.class})
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(source = "specialtySet", target = "specialtyDTOSet")
    @Mapping(source = "permanentAddress", target = "permanentAddressDTO")
    DoctorDTO toDoctorDTO(Doctor doctor);

    @Mapping(source = "specialtyDTOSet", target = "specialtySet")
    @Mapping(source = "permanentAddressDTO", target = "permanentAddress")
    Doctor toDoctor(DoctorDTO doctorDTO);

    DoctorDTO toDoctorDTO(DoctorCreateDTO personCreateDTO);

    DoctorDTO toDoctorDTO(PersonDTO personDTO);

    List<DoctorDTO> toDoctorDTOCollection(Collection<Doctor> doctorCollection);

    List<Doctor> toDoctorCollection(Collection<DoctorDTO> doctorDTOCollection);

}