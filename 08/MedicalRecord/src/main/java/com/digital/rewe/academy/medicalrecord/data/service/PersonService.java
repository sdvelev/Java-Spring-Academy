package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> getPersons();
    PersonDTO getPerson(Long id);
    Person getPersonEntity(Long id);
    PersonDTO createPerson(PersonCreateDTO personCreateDTO);
    PersonDTO updatePerson(PersonDTO personDTO, Long id);
    void deletePerson(Long id);
    Person setPersonNonNullFields(PersonDTO personDTO, Person personToUpdate);
}