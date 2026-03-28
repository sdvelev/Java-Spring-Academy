package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getPersons();

    PersonDTO getPerson(Long id);

    PersonDTO createPerson(PersonCreateDTO personCreateDTO);

    PersonDTO updatePerson(PersonDTO personDTO, Long id);

    Person setPersonNonNullFields(PersonDTO personDTO, Person personToUpdate);

    void deletePerson(Long id);
}