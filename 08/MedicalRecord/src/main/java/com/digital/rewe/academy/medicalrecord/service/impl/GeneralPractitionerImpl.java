package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.GeneralPractitionerRepository;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerCreateDTO;
import com.digital.rewe.academy.medicalrecord.service.AddressService;
import com.digital.rewe.academy.medicalrecord.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.exception.GeneralPractitionerNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import com.digital.rewe.academy.medicalrecord.mapper.GeneralPractitionerMapper;
import com.digital.rewe.academy.medicalrecord.service.RoleService;
import com.digital.rewe.academy.medicalrecord.service.UserService;
import com.digital.rewe.academy.medicalrecord.util.UserRole;
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

    private final UserService userService;
    private final RoleService roleService;

    @Override
    public List<GeneralPractitionerDTO> getGeneralPractitioners() {
        return generalPractitionerMapper.toGeneralPractitionerDTOCollection(generalPractitionerRepository.findAll());
    }

    @Override
    public GeneralPractitionerDTO getGeneralPractitioner(Long id) {
        return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.findById(id)
            .orElseThrow(() -> new GeneralPractitionerNotFoundException("Such a general practitioner not found", id)));
    }

    @Override
    @Transactional
    public GeneralPractitionerDTO createGeneralPractitioner(GeneralPractitionerCreateDTO generalPractitionerCreateDTO) {
        GeneralPractitionerDTO generalPractitionerDTOToSave = generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerCreateDTO);
        generalPractitionerDTOToSave.setPermanentAddressDTO(addressService.getAddress(generalPractitionerCreateDTO.getPermanentAddressId()));
        generalPractitionerDTOToSave.setPracticeAddressDTO(addressService.getAddress(generalPractitionerCreateDTO.getPracticeAddressId()));
        GeneralPractitioner savedGeneralPractitioner = generalPractitionerRepository.save(generalPractitionerMapper.toGeneralPractitioner(generalPractitionerDTOToSave));

        Role roleToAdd = roleService.getRoleByName(UserRole.DOCTOR.name());
        User savedUser = userService.findUserById(savedGeneralPractitioner.getId());
        roleToAdd.getUsers().add(savedUser);
        roleService.saveRole(roleToAdd);

        return generalPractitionerMapper.toGeneralPractitionerDTO(savedGeneralPractitioner);
    }

//    @Override
//    public GeneralPractitionerDTO createGeneralPractitionerWithExistingAddresses(
//        GeneralPractitionerDTO generalPractitionerDTO, Long permanentAddressId, Long practiceAddressId) {
//        GeneralPractitioner generalPractitionerToSave = new GeneralPractitioner();
//
//        generalPractitionerToSave = setGeneralPractitionerNonNullFields(generalPractitionerDTO, generalPractitionerToSave);
//
//        generalPractitionerToSave.setPermanentAddress(addressMapper.toAddress(addressService.getAddress(permanentAddressId)));
//        generalPractitionerToSave.setPracticeAddress(addressMapper.toAddress(addressService.getAddress(practiceAddressId)));
//
//        return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.save(generalPractitionerToSave));
//    }

    @Override
    public GeneralPractitionerDTO updateGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO, Long id) {
        Optional<GeneralPractitioner> optionalGeneralPractitioner = generalPractitionerRepository.findById(id);
        if (optionalGeneralPractitioner.isPresent()) {
            GeneralPractitioner generalPractitionerToUpdate = setGeneralPractitionerNonNullFields(generalPractitionerDTO,
                optionalGeneralPractitioner.get());
            return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.save(generalPractitionerToUpdate));
        }

        throw new GeneralPractitionerNotFoundException("Such a general practitioner not found", id);
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

        return generalPractitionerToUpdate;
    }
}