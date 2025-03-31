package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.data.repo.PersonRepository;
import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.data.service.PersonService;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import com.digital.rewe.academy.medicalrecord.mapper.PersonMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    private final AddressService addressService;

    @Override
    public List<PersonDTO> getPersons() {
        return personMapper.toPersonDTOCollection(personRepository.findAll());
    }

    @Override
    public PersonDTO getPerson(Long id) {
        return personMapper.toPersonDTO(personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person with id " + id + " not found")));
    }

    @Override
    public PersonDTO createPerson(PersonCreateDTO personCreateDTO) {
        PersonDTO personDTOToSave = personMapper.toPersonDTO(personCreateDTO);
        personDTOToSave.setPermanentAddressDTO(addressService.getAddress(personCreateDTO.getPermanentAddressId()));
        return personMapper.toPersonDTO(personRepository.save(personMapper.toPerson(personDTOToSave)));
    }

    @Override
    public PersonDTO updatePerson(PersonDTO personDTO, Long id) {
        Optional<Person> optionalPersonToUpdate = personRepository.findById(id);
        if (optionalPersonToUpdate.isPresent()) {
            Person personToUpdate = setPersonNonNullFields(personDTO, optionalPersonToUpdate.get());
            return personMapper.toPersonDTO(personRepository.save(personToUpdate));
        }

        throw new EntityNotFoundException("Person with id " + id + " not found");
    }

    @Override
    public Person setPersonNonNullFields(PersonDTO personDTO, Person personToUpdate) {
        if (personDTO.getFirstName() != null) {
            personToUpdate.setFirstName(personDTO.getFirstName());
        }

        if (personDTO.getMiddleName() != null) {
            personToUpdate.setMiddleName(personDTO.getMiddleName());
        }

        if (personDTO.getLastName() != null) {
            personToUpdate.setLastName(personDTO.getLastName());
        }

        if (personDTO.getPersonalIdNumber() != null) {
            personToUpdate.setPersonalIdNumber(personDTO.getPersonalIdNumber());
        }

        if (personDTO.getBirthDate() != null) {
            personToUpdate.setBirthDate(personDTO.getBirthDate());
        }

        if (personDTO.getPhoneNumber() != null) {
            personToUpdate.setPhoneNumber(personDTO.getPhoneNumber());
        }

        if (personDTO.getEmail() != null) {
            personToUpdate.setEmail(personDTO.getEmail());
        }

        return personToUpdate;
    }

    @Override
    public void deletePerson(Long id) {
        this.personRepository.deleteById(id);
    }
}