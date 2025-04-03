package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.GeneralPractitionerRepository;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerCreateDTO;
import com.digital.rewe.academy.medicalrecord.service.AddressService;
import com.digital.rewe.academy.medicalrecord.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.exception.GeneralPractitionerNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.GeneralPractitionerMapper;
import com.digital.rewe.academy.medicalrecord.service.RoleService;
import com.digital.rewe.academy.medicalrecord.service.UserService;
import com.digital.rewe.academy.medicalrecord.util.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralPractitionerImpl implements GeneralPractitionerService {

    private final GeneralPractitionerRepository generalPractitionerRepository;
    private final GeneralPractitionerMapper generalPractitionerMapper;

    private final AddressService addressService;

    private final DoctorService doctorService;

    private final UserService userService;

    private final RoleService roleService;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<GeneralPractitionerDTO> getGeneralPractitioners() {
        return generalPractitionerMapper.toGeneralPractitionerDTOCollection(generalPractitionerRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public GeneralPractitionerDTO getGeneralPractitioner(Long id) {
        User currentUser = getUserFromAuthenticationContext();
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())) {
            return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.findById(currentUser.getId())
                .orElseThrow(() -> new GeneralPractitionerNotFoundException("Such a general practitioner not found", currentUser.getId())));
        }

        return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.findById(id)
            .orElseThrow(() -> new GeneralPractitionerNotFoundException("Such a general practitioner not found", id)));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public GeneralPractitionerDTO createGeneralPractitioner(GeneralPractitionerCreateDTO generalPractitionerCreateDTO) {
        GeneralPractitionerDTO generalPractitionerDTOToSave = generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerCreateDTO);
        generalPractitionerDTOToSave.setPermanentAddressDTO(addressService.getAddress(generalPractitionerCreateDTO.getPermanentAddressId()));
        generalPractitionerDTOToSave.setPracticeAddressDTO(addressService.getAddress(generalPractitionerCreateDTO.getPracticeAddressId()));
        GeneralPractitioner generalPractitionerToSave = generalPractitionerMapper.toGeneralPractitioner(generalPractitionerDTOToSave);
        generalPractitionerToSave.setPersonalIdNumber(generalPractitionerCreateDTO.getPersonalIdNumber());
        GeneralPractitioner savedGeneralPractitioner = generalPractitionerRepository.save(generalPractitionerToSave);

        Role roleToAdd = roleService.getRoleByName(UserRole.DOCTOR.name());
        User savedUser = userService.findUserById(savedGeneralPractitioner.getId());
        roleToAdd.getUsers().add(savedUser);
        roleService.saveRole(roleToAdd);

        return generalPractitionerMapper.toGeneralPractitionerDTO(savedGeneralPractitioner);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public GeneralPractitionerDTO updateGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO, Long id) {
        Optional<GeneralPractitioner> optionalGeneralPractitioner = generalPractitionerRepository.findById(id);
        if (optionalGeneralPractitioner.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
                && !optionalGeneralPractitioner.get().getId().equals(currentUser.getId())) {
                optionalGeneralPractitioner = generalPractitionerRepository.findById(currentUser.getId());
            }
            if (optionalGeneralPractitioner.isEmpty()) {
                throw new GeneralPractitionerNotFoundException("Such a general practitioner not found", id);
            }
            GeneralPractitioner generalPractitionerToUpdate = setGeneralPractitionerNonNullFields(generalPractitionerDTO,
                optionalGeneralPractitioner.get());
            return generalPractitionerMapper.toGeneralPractitionerDTO(generalPractitionerRepository.save(generalPractitionerToUpdate));
        }

        throw new GeneralPractitionerNotFoundException("Such a general practitioner not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteGeneralPractitioner(Long id) {
        this.generalPractitionerRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public GeneralPractitioner setGeneralPractitionerNonNullFields(GeneralPractitionerDTO generalPractitionerDTO,
                                                                   GeneralPractitioner generalPractitionerToUpdate) {
        doctorService.setDoctorNonNullFields(generalPractitionerDTO, generalPractitionerToUpdate);
        if (generalPractitionerDTO.getPracticeNumber() != null) {
            generalPractitionerToUpdate.setPracticeNumber(generalPractitionerDTO.getPracticeNumber());
        }

        return generalPractitionerToUpdate;
    }

    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}