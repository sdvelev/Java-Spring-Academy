package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.data.repo.DoctorRepository;
import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.data.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.data.service.PersonService;
import com.digital.rewe.academy.medicalrecord.data.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import com.digital.rewe.academy.medicalrecord.mapper.DoctorMapper;
import com.digital.rewe.academy.medicalrecord.mapper.SpecialtyMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    private final PersonService personService;
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    private final SpecialtyService specialtyService;
    private final SpecialtyMapper specialtyMapper;


    @Override
    public List<DoctorDTO> getDoctors() {
        return doctorMapper.toDoctorDTOCollection(doctorRepository.findAll());
    }

    @Override
    public DoctorDTO getDoctor(Long id) {
        return doctorMapper.toDoctorDTO(doctorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Doctor with id: " + id + " not found")));
    }

    @Override
    public List<DoctorDTO> getDoctorWithMostSickLeaves() {
        return doctorMapper.toDoctorDTOCollection(doctorRepository.findDoctorWithMostSickLeaves());
    }

   /* @Override
    public DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO) {
        PersonDTO relevantPersonDTO = personService.getPerson(doctorCreateDTO.getPersonId());
        Doctor doctorToSave = new Doctor();
//        doctorToSave.setId(doctorCreateDTO.getPersonId());
        doctorToSave.setUniqueIdNumber(doctorCreateDTO.getUniqueIdNumber());
        doctorToSave.setFirstName(relevantPersonDTO.getFirstName());
        doctorToSave.setMiddleName(relevantPersonDTO.getMiddleName());
        doctorToSave.setLastName(relevantPersonDTO.getLastName());
        doctorToSave.setUsername(relevantPersonDTO.getUsername());
        doctorToSave.setPersonalIdNumber(relevantPersonDTO.getPersonalIdNumber());
        doctorToSave.setBirthDate(relevantPersonDTO.getBirthDate());
        doctorToSave.setPhoneNumber(relevantPersonDTO.getPhoneNumber());
        doctorToSave.setEmail(relevantPersonDTO.getEmail());
        doctorToSave.setPermanentAddress(addressMapper.toAddress(relevantPersonDTO.getPermanentAddressDTO()));
//        doctorMapper.toDoctorDTO(relevantPersonDTO);
//        Doctor doctorToSave = doctorMapper.toDoctor(doctorDTOToSave);
        return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
    }*/

//    @Override
//    public DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO) {
//        // Retrieve the existing Person from the PersonService
//        Person relevantPerson = personService.getPersonEntity(doctorCreateDTO.getPersonId());
//        if (relevantPerson == null) {
//            throw new EntityNotFoundException("Person with id " + doctorCreateDTO.getPersonId() + " not found");
//        }
//
//        Doctor doctorToSave = new Doctor();
//        doctorToSave.setId(relevantPerson.getId());  // Set the same ID as the existing Person
//        doctorToSave.setUniqueIdNumber(doctorCreateDTO.getUniqueIdNumber());
//        doctorToSave.setFirstName(relevantPerson.getFirstName());
//        doctorToSave.setMiddleName(relevantPerson.getMiddleName());
//        doctorToSave.setLastName(relevantPerson.getLastName());
//        doctorToSave.setUsername(relevantPerson.getUsername());
//        doctorToSave.setPersonalIdNumber(relevantPerson.getPersonalIdNumber());
//        doctorToSave.setBirthDate(relevantPerson.getBirthDate());
//        doctorToSave.setPhoneNumber(relevantPerson.getPhoneNumber());
//        doctorToSave.setEmail(relevantPerson.getEmail());
//        doctorToSave.setPermanentAddress(relevantPerson.getPermanentAddress());
//
//        return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
//    }

    @Override
    @Transactional
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {


        Doctor doctorToSave = new Doctor();
//        doctorToSave.setUniqueIdNumber(doctorDTO.getUniqueIdNumber());
//        doctorToSave.setFirstName(doctorDTO.getFirstName());
//        doctorToSave.setMiddleName(doctorDTO.getMiddleName());
//        doctorToSave.setLastName(doctorDTO.getLastName());
//        doctorToSave.setUsername(doctorDTO.getUsername());
//        doctorToSave.setPersonalIdNumber(doctorDTO.getPersonalIdNumber());
//        doctorToSave.setBirthDate(doctorDTO.getBirthDate());
//        doctorToSave.setPhoneNumber(doctorDTO.getPhoneNumber());
//        doctorToSave.setEmail(doctorDTO.getEmail());

        doctorToSave = setDoctorNonNullFields(doctorDTO, doctorToSave);

        AddressDTO addressToSave = addressService.createAddress(doctorDTO.getPermanentAddressDTO());

        doctorToSave.setPermanentAddress(addressMapper.toAddress(addressToSave));

        return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
    }

    @Override
    public DoctorDTO createDoctorWithExistingAddress(DoctorDTO doctorDTO, Long addressId) {

        // Retrieve the existing Person from the PersonService
        Doctor doctorToSave = new Doctor();
//        doctorToSave.setUniqueIdNumber(doctorDTO.getUniqueIdNumber());
//        doctorToSave.setFirstName(doctorDTO.getFirstName());
//        doctorToSave.setMiddleName(doctorDTO.getMiddleName());
//        doctorToSave.setLastName(doctorDTO.getLastName());
//        doctorToSave.setUsername(doctorDTO.getUsername());
//        doctorToSave.setPersonalIdNumber(doctorDTO.getPersonalIdNumber());
//        doctorToSave.setBirthDate(doctorDTO.getBirthDate());
//        doctorToSave.setPhoneNumber(doctorDTO.getPhoneNumber());
//        doctorToSave.setEmail(doctorDTO.getEmail());

        doctorToSave = setDoctorNonNullFields(doctorDTO, doctorToSave);

        doctorToSave.setPermanentAddress(addressMapper.toAddress(addressService.getAddress(addressId)));

        return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
    }


    @Override
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO, Long id) {
        Optional<Doctor> optionalDoctorToUpdate = doctorRepository.findById(id);
        if (optionalDoctorToUpdate.isPresent()) {
            Doctor doctorToUpdate = setDoctorNonNullFields(doctorDTO, optionalDoctorToUpdate.get());
            return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToUpdate));
        }

        throw new EntityNotFoundException("Doctor with id " + id + " not found");
    }

    @Override
    public void deleteDoctor(Long id) {
        this.doctorRepository.deleteById(id);
    }

    @Override
    public Doctor setDoctorNonNullFields(DoctorDTO doctorDTO, Doctor doctorToUpdate) {
        if (doctorDTO.getUniqueIdNumber() != null) {
            doctorToUpdate.setUniqueIdNumber(doctorDTO.getUniqueIdNumber());
        }

        personService.setPersonNonNullFields(doctorDTO, doctorToUpdate);
//        if (doctorDTO.getFirstName() != null) {
//            doctorToUpdate.setFirstName(doctorDTO.getFirstName());
//        }
//
//        if (doctorDTO.getMiddleName() != null) {
//            doctorToUpdate.setMiddleName(doctorDTO.getMiddleName());
//        }
//
//        if (doctorDTO.getLastName() != null) {
//            doctorToUpdate.setLastName(doctorDTO.getLastName());
//        }
//
//        if (doctorDTO.getUsername() != null) {
//            doctorToUpdate.setUsername(doctorDTO.getUsername());
//        }
//
//        if (doctorDTO.getPersonalIdNumber() != null) {
//            doctorToUpdate.setPersonalIdNumber(doctorDTO.getPersonalIdNumber());
//        }
//
//        if (doctorDTO.getBirthDate() != null) {
//            doctorToUpdate.setBirthDate(doctorDTO.getBirthDate());
//        }
//
//        if (doctorDTO.getPhoneNumber() != null) {
//            doctorToUpdate.setPhoneNumber(doctorDTO.getPhoneNumber());
//        }
//
//        if (doctorDTO.getEmail() != null) {
//            doctorToUpdate.setEmail(doctorDTO.getEmail());
//        }

        return doctorToUpdate;
    }

    @Override
    public DoctorDTO addSpecialty(Long doctorId, Long specialtyId) {
        Optional<Doctor> optionalDoctorToAddSpecialty = doctorRepository.findById(doctorId);
        if (optionalDoctorToAddSpecialty.isPresent()) {
            Doctor doctorToSave = optionalDoctorToAddSpecialty.get();
            SpecialtyDTO specialtyDTO = specialtyService.getSpecialty(specialtyId);
            doctorToSave.getSpecialtySet().add(specialtyMapper.toSpecialty(specialtyDTO));
            return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
        }

        throw new RuntimeException("Doctor with id: " + doctorId + " not found");
    }

    @Override
    public DoctorDTO removeSpecialty(Long doctorId, Long specialtyId) {
        Optional<Doctor> optionalDoctorToRemoveSpecialty = doctorRepository.findById(doctorId);
        if (optionalDoctorToRemoveSpecialty.isPresent()) {
            Doctor doctorToSave = optionalDoctorToRemoveSpecialty.get();
            SpecialtyDTO specialtyDTO = specialtyService.getSpecialty(specialtyId);
            doctorToSave.getSpecialtySet().remove(specialtyMapper.toSpecialty(specialtyDTO));
            return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
        }

        throw new RuntimeException("Doctor with id: " + doctorId + " not found");
    }
}
