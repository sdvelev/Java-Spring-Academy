package com.digital.rewe.academy.medicalrecord.mapper;

import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "permanentAddress", target = "permanentAddressDTO")
    PersonDTO toPersonDTO(Person person);

    @Mapping(source = "permanentAddressDTO", target = "permanentAddress")
    Person toPerson(PersonDTO personDTO);

    PersonDTO toPersonDTO(PersonCreateDTO personCreateDTO);

    List<PersonDTO> toPersonDTOCollection(Collection<Person> personCollection);

    List<Person> toPersonCollection(Collection<PersonDTO> personDTOCollection);
}