package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.PersonRepository;
import com.digital.rewe.academy.medicalrecord.service.AddressService;
import com.digital.rewe.academy.medicalrecord.service.PersonService;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import com.digital.rewe.academy.medicalrecord.exception.PersonNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.PersonMapper;
import com.digital.rewe.academy.medicalrecord.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public List<PersonDTO> getPersons() {
        return personMapper.toPersonDTOCollection(personRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public PersonDTO getPerson(Long id) {
        User currentUser = getUserFromAuthenticationContext();
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.PATIENT.name()).build())) {
            return personMapper.toPersonDTO(personRepository.findById(currentUser.getId())
                .orElseThrow(() -> new PersonNotFoundException("Such a person not found", currentUser.getId())));
        }

        return personMapper.toPersonDTO(personRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException("Such a person not found", id)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public PersonDTO createPerson(PersonCreateDTO personCreateDTO) {
        PersonDTO personDTOToSave = personMapper.toPersonDTO(personCreateDTO);
        personDTOToSave.setPermanentAddressDTO(addressService.getAddress(personCreateDTO.getPermanentAddressId()));
        return personMapper.toPersonDTO(personRepository.save(personMapper.toPerson(personDTOToSave)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public PersonDTO updatePerson(PersonDTO personDTO, Long id) {
        Optional<Person> optionalPersonToUpdate = personRepository.findById(id);
        if (optionalPersonToUpdate.isPresent()) {
            Person personToUpdate = setPersonNonNullFields(personDTO, optionalPersonToUpdate.get());
            return personMapper.toPersonDTO(personRepository.save(personToUpdate));
        }

        throw new PersonNotFoundException("Such a person not found", id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
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
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public void deletePerson(Long id) {
        this.personRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}