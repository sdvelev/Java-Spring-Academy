package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.PersonService;
import com.digital.rewe.academy.medicalrecord.dto.PersonCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<PersonDTO> getPersons() {
        return personService.getPersons();
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable @Valid @Positive Long id) {
        return personService.getPerson(id);
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody @Valid PersonCreateDTO personCreateDTO) {
        return personService.createPerson(personCreateDTO);
    }

    @PutMapping("/{id}")
    public PersonDTO updatePerson(@RequestBody @Valid PersonDTO person, @PathVariable @Positive Long id) {
        return this.personService.updatePerson(person, id);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable @Valid @Positive Long id) {
        this.personService.deletePerson(id);
    }
}