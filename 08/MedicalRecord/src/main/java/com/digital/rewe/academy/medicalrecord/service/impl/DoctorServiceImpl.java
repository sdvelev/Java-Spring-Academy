package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.DoctorRepository;
import com.digital.rewe.academy.medicalrecord.dto.DoctorCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;
import com.digital.rewe.academy.medicalrecord.exception.GeneralPractitionerNotFoundException;
import com.digital.rewe.academy.medicalrecord.exception.PatientNotFoundException;
import com.digital.rewe.academy.medicalrecord.service.AddressService;
import com.digital.rewe.academy.medicalrecord.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.service.PersonService;
import com.digital.rewe.academy.medicalrecord.service.RoleService;
import com.digital.rewe.academy.medicalrecord.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
import com.digital.rewe.academy.medicalrecord.exception.DoctorNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import com.digital.rewe.academy.medicalrecord.mapper.DoctorMapper;
import com.digital.rewe.academy.medicalrecord.mapper.SpecialtyMapper;
import com.digital.rewe.academy.medicalrecord.service.UserService;
import com.digital.rewe.academy.medicalrecord.util.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private final UserService userService;
    private final RoleService roleService;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DoctorDTO> getDoctors() {
        return doctorMapper.toDoctorDTOCollection(doctorRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public DoctorDTO getDoctor(Long id) {
        User currentUser = getUserFromAuthenticationContext();
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())) {
            return doctorMapper.toDoctorDTO(doctorRepository.findById(currentUser.getId())
                .orElseThrow(() -> new DoctorNotFoundException("Such a doctor not found", currentUser.getId())));
        }

        return doctorMapper.toDoctorDTO(doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Such a doctor not found", id)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<DoctorDTO> getDoctorWithMostSickLeaves() {
        return doctorMapper.toDoctorDTOCollection(doctorRepository.findDoctorWithMostSickLeaves());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO) {
        DoctorDTO doctorDTOToSave = doctorMapper.toDoctorDTO(doctorCreateDTO);
        doctorDTOToSave.setPermanentAddressDTO(addressService.getAddress(doctorCreateDTO.getPermanentAddressId()));
        Doctor savedDoctor = doctorRepository.save(doctorMapper.toDoctor(doctorDTOToSave));

        Role roleToAdd = roleService.getRoleByName(UserRole.DOCTOR.name());
        User savedUser = userService.findUserById(savedDoctor.getId());
        roleToAdd.getUsers().add(savedUser);
        roleService.saveRole(roleToAdd);
        return doctorMapper.toDoctorDTO(savedDoctor);
    }

//    @Override
//    public DoctorDTO createDoctorWithExistingAddress(DoctorDTO doctorDTO, Long addressId) {
//        Doctor doctorToSave = new Doctor();
//        doctorToSave = setDoctorNonNullFields(doctorDTO, doctorToSave);
//
//        doctorToSave.setPermanentAddress(addressMapper.toAddress(addressService.getAddress(addressId)));
//        return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
//    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO, Long id) {
        Optional<Doctor> optionalDoctorToUpdate = doctorRepository.findById(id);
        if (optionalDoctorToUpdate.isPresent()) {
            User currentUser = getUserFromAuthenticationContext();
            if (currentUser != null && currentUser.getAuthorities()
                .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())
            && !optionalDoctorToUpdate.get().getId().equals(currentUser.getId())) {
                optionalDoctorToUpdate = doctorRepository.findById(currentUser.getId());
            }
            if (optionalDoctorToUpdate.isEmpty()) {
                throw new DoctorNotFoundException("Such a doctor not found", id);
            }
            Doctor doctorToUpdate = setDoctorNonNullFields(doctorDTO, optionalDoctorToUpdate.get());
            return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToUpdate));
        }

        throw new DoctorNotFoundException("Such a doctor not found", id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDoctor(Long id) {
        this.doctorRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public Doctor setDoctorNonNullFields(DoctorDTO doctorDTO, Doctor doctorToUpdate) {
        if (doctorDTO.getUniqueIdNumber() != null) {
            doctorToUpdate.setUniqueIdNumber(doctorDTO.getUniqueIdNumber());
        }

        personService.setPersonNonNullFields(doctorDTO, doctorToUpdate);
        return doctorToUpdate;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public DoctorDTO addSpecialty(Long doctorId, Long specialtyId) {
        Optional<Doctor> optionalDoctorToAddSpecialty = doctorRepository.findById(doctorId);
        if (optionalDoctorToAddSpecialty.isPresent()) {
            Doctor doctorToSave = optionalDoctorToAddSpecialty.get();
            SpecialtyDTO specialtyDTO = specialtyService.getSpecialty(specialtyId);
            doctorToSave.getSpecialtySet().add(specialtyMapper.toSpecialty(specialtyDTO));
            return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
        }

        throw new DoctorNotFoundException("Such a doctor not found", doctorId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public DoctorDTO removeSpecialty(Long doctorId, Long specialtyId) {
        Optional<Doctor> optionalDoctorToRemoveSpecialty = doctorRepository.findById(doctorId);
        if (optionalDoctorToRemoveSpecialty.isPresent()) {
            Doctor doctorToSave = optionalDoctorToRemoveSpecialty.get();
            SpecialtyDTO specialtyDTO = specialtyService.getSpecialty(specialtyId);
            doctorToSave.getSpecialtySet().remove(specialtyMapper.toSpecialty(specialtyDTO));
            return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
        }

        throw new DoctorNotFoundException("Such a doctor not found", doctorId);
    }

    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}