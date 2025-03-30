package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import com.digital.rewe.academy.medicalrecord.data.repo.GeneralPractitionerRepository;
import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.data.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.data.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import com.digital.rewe.academy.medicalrecord.mapper.GeneralPractitionerMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralPractitionerImpl implements GeneralPractitionerService {

    private final GeneralPractitionerRepository generalPractitionerRepository;
    private final GeneralPractitionerMapper generalPractitionerMapper;

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    private final DoctorService doctorService;

    @Override
    public List<GeneralPractitionerDTO> getGeneralPractitioners() {
        return generalPractitionerMapper.toGeneralPractitionerDTOCollection(generalPractitionerRepository.findAll());
    }

    @Override
    public GeneralPractitionerDTO getGeneralPractitioner(Long id) {
        return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("General practitioner with id: " + id + " not found")));
    }

    @Override
    @Transactional
    public GeneralPractitionerDTO createGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO) {
        // AddressDTO addressDTO = addressService.getAddress(addressId);

        // Retrieve the existing Person from the PersonService
        GeneralPractitioner generalPractitionerToSave = new GeneralPractitioner();
//        doctorToSave.setId(doctorDTO.getId());  // Set the same ID as the existing Person
//        generalPractitionerToSave.setPracticeNumber(generalPractitionerDTO.getPracticeNumber());
//        generalPractitionerToSave.setUniqueIdNumber(generalPractitionerDTO.getUniqueIdNumber());
//        generalPractitionerToSave.setFirstName(generalPractitionerDTO.getFirstName());
//        generalPractitionerToSave.setMiddleName(generalPractitionerDTO.getMiddleName());
//        generalPractitionerToSave.setLastName(generalPractitionerDTO.getLastName());
//        generalPractitionerToSave.setUsername(generalPractitionerDTO.getUsername());
//        generalPractitionerToSave.setPersonalIdNumber(generalPractitionerDTO.getPersonalIdNumber());
//        generalPractitionerToSave.setBirthDate(generalPractitionerDTO.getBirthDate());
//        generalPractitionerToSave.setPhoneNumber(generalPractitionerDTO.getPhoneNumber());
//        generalPractitionerToSave.setEmail(generalPractitionerDTO.getEmail());

        generalPractitionerToSave = this.setGeneralPractitionerNonNullFields(generalPractitionerDTO, generalPractitionerToSave);

        AddressDTO permanentAddressToSave = addressService.createAddress(generalPractitionerDTO.getPermanentAddressDTO());
        generalPractitionerToSave.setPermanentAddress(addressMapper.toAddress(permanentAddressToSave));

        AddressDTO practiceAddressToSave = addressService.createAddress(generalPractitionerDTO.getPracticeAddressDTO());
        generalPractitionerToSave.setPracticeAddress(addressMapper.toAddress(practiceAddressToSave));

        return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.save(generalPractitionerToSave));
    }

    @Override
    public GeneralPractitionerDTO createGeneralPractitionerWithExistingAddresses(
        GeneralPractitionerDTO generalPractitionerDTO, Long permanentAddressId, Long practiceAddressId) {
        GeneralPractitioner generalPractitionerToSave = new GeneralPractitioner();
//        doctorToSave.setId(doctorDTO.getId());  // Set the same ID as the existing Person
//        generalPractitionerToSave.setPracticeNumber(generalPractitionerDTO.getPracticeNumber());
//        generalPractitionerToSave.setUniqueIdNumber(generalPractitionerDTO.getUniqueIdNumber());
//        generalPractitionerToSave.setFirstName(generalPractitionerDTO.getFirstName());
//        generalPractitionerToSave.setMiddleName(generalPractitionerDTO.getMiddleName());
//        generalPractitionerToSave.setLastName(generalPractitionerDTO.getLastName());
//        generalPractitionerToSave.setUsername(generalPractitionerDTO.getUsername());
//        generalPractitionerToSave.setPersonalIdNumber(generalPractitionerDTO.getPersonalIdNumber());
//        generalPractitionerToSave.setBirthDate(generalPractitionerDTO.getBirthDate());
//        generalPractitionerToSave.setPhoneNumber(generalPractitionerDTO.getPhoneNumber());
//        generalPractitionerToSave.setEmail(generalPractitionerDTO.getEmail());

        generalPractitionerToSave = this.setGeneralPractitionerNonNullFields(generalPractitionerDTO, generalPractitionerToSave);

        generalPractitionerToSave.setPermanentAddress(addressMapper.toAddress(addressService.getAddress(permanentAddressId)));
        generalPractitionerToSave.setPracticeAddress(addressMapper.toAddress(addressService.getAddress(practiceAddressId)));

        return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.save(generalPractitionerToSave));
    }

    @Override
    public GeneralPractitionerDTO updateGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO, Long id) {
        Optional<GeneralPractitioner> optionalGeneralPractitioner = generalPractitionerRepository.findById(id);
        if (optionalGeneralPractitioner.isPresent()) {
            GeneralPractitioner generalPractitionerToUpdate = setGeneralPractitionerNonNullFields(generalPractitionerDTO, optionalGeneralPractitioner.get());
            return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.save(generalPractitionerToUpdate));
        }

        throw new EntityNotFoundException("General practitioner with id " + id + " not found");
    }

    @Override
    public void deleteGeneralPractitioner(Long id) {
        this.generalPractitionerRepository.deleteById(id);
    }

    @Override
    public GeneralPractitioner setGeneralPractitionerNonNullFields(GeneralPractitionerDTO generalPractitionerDTO,
                                                                   GeneralPractitioner generalPractitionerToUpdate) {
        doctorService.setDoctorNonNullFields(generalPractitionerDTO, generalPractitionerToUpdate);

        if (generalPractitionerDTO.getPracticeNumber() != null) {
            generalPractitionerToUpdate.setPracticeNumber(generalPractitionerDTO.getPracticeNumber());
        }

//        if (generalPractitionerDTO.getUniqueIdNumber() != null) {
//            generalPractitionerToUpdate.setUniqueIdNumber(generalPractitionerDTO.getFirstName());
//        }
//
//        if (generalPractitionerDTO.getFirstName() != null) {
//            generalPractitionerToUpdate.setFirstName(generalPractitionerDTO.getFirstName());
//        }
//
//        if (generalPractitionerDTO.getMiddleName() != null) {
//            generalPractitionerToUpdate.setMiddleName(generalPractitionerDTO.getMiddleName());
//        }
//
//        if (generalPractitionerDTO.getLastName() != null) {
//            generalPractitionerToUpdate.setLastName(generalPractitionerDTO.getLastName());
//        }
//
//        if (generalPractitionerDTO.getUsername() != null) {
//            generalPractitionerToUpdate.setUsername(generalPractitionerDTO.getUsername());
//        }
//
//        if (generalPractitionerDTO.getPersonalIdNumber() != null) {
//            generalPractitionerToUpdate.setPersonalIdNumber(generalPractitionerDTO.getPersonalIdNumber());
//        }
//
//        if (generalPractitionerDTO.getBirthDate() != null) {
//            generalPractitionerToUpdate.setBirthDate(generalPractitionerDTO.getBirthDate());
//        }
//
//        if (generalPractitionerDTO.getPhoneNumber() != null) {
//            generalPractitionerToUpdate.setPhoneNumber(generalPractitionerDTO.getPhoneNumber());
//        }
//
//        if (generalPractitionerDTO.getEmail() != null) {
//            generalPractitionerToUpdate.setEmail(generalPractitionerDTO.getEmail());
//        }

        return generalPractitionerToUpdate;
    }
}
