package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.PatientRepository;
import com.digital.rewe.academy.medicalrecord.dto.PatientCreateDTO;
import com.digital.rewe.academy.medicalrecord.service.AddressService;
import com.digital.rewe.academy.medicalrecord.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.service.PatientService;
import com.digital.rewe.academy.medicalrecord.service.PersonService;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientCountPerGeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import com.digital.rewe.academy.medicalrecord.exception.PatientNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.GeneralPractitionerMapper;
import com.digital.rewe.academy.medicalrecord.mapper.PatientMapper;
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
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    private final AddressService addressService;

    private final PersonService personService;

    private final GeneralPractitionerService generalPractitionerService;
    private final GeneralPractitionerMapper generalPractitionerMapper;

    private final UserService userService;

    private final RoleService roleService;

    @Override
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public List<PatientDTO> getPatients() {
        return patientMapper.toPatientDTOCollection(patientRepository.findAll());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PatientDTO> getPatientsWithSpecificDiagnose(Long diagnoseId) {
        return patientMapper.toPatientDTOCollection(patientRepository.findDistinctByMedicalvisitSet_DiagnoseSet_Id(diagnoseId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public List<PatientDTO> getPatientsWithGeneralPractitioner(Long generalPractitionerId) {
        User currentUser = getUserFromAuthenticationContext();
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.DOCTOR.name()).build())) {
            generalPractitionerId = currentUser.getId();
        }

        return patientMapper.toPatientDTOCollection(patientRepository.findByGeneralPractitionerId(generalPractitionerId));
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Long getCountByGeneralPractitionerId(Long generalPractitionerId) {
        return patientRepository.countByGeneralPractitionerId(generalPractitionerId);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PatientCountPerGeneralPractitionerDTO> getCountPerGeneralPractitioner() {
        return patientRepository.findPatientCountPerGp();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public PatientDTO getPatient(Long id) {
        User currentUser = getUserFromAuthenticationContext();
        if (currentUser != null && currentUser.getAuthorities()
            .contains(Role.builder().authority(UserRole.PATIENT.name()).build())) {
            return patientMapper.toPatientDTO(patientRepository.findById(currentUser.getId())
                .orElseThrow(() -> new PatientNotFoundException("Such a patient not found", currentUser.getId())));
        }

        return patientMapper.toPatientDTO(patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Such a patient not found", id)));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public PatientDTO createPatient(PatientCreateDTO patientCreateDTO) {
        PatientDTO patientDTOToSave = patientMapper.toPatientDTO(patientCreateDTO);
        patientDTOToSave.setPermanentAddressDTO(addressService.getAddress(patientCreateDTO.getPermanentAddressId()));
        Patient patientToSave = patientMapper.toPatient(patientDTOToSave);
        patientToSave.setPersonalIdNumber(patientCreateDTO.getPersonalIdNumber());
        Patient savedPatient = patientRepository.save(patientToSave);

        Role roleToAdd = roleService.getRoleByName(UserRole.PATIENT.name());
        User savedUser = userService.findUserById(savedPatient.getId());
        roleToAdd.getUsers().add(savedUser);
        roleService.saveRole(roleToAdd);

        return patientMapper.toPatientDTO(savedPatient);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public PatientDTO updatePatient(PatientDTO patientDTO, Long id) {
        Optional<Patient> optionalPatientToUpdate = patientRepository.findById(id);
        if (optionalPatientToUpdate.isPresent()) {
            Patient patientToUpdate = setPatientNonNullFields(patientDTO, optionalPatientToUpdate.get());
            return patientMapper.toPatientDTO(patientRepository.save(patientToUpdate));
        }

        throw new PatientNotFoundException("Such a patient not found", id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deletePatient(Long id) {
        this.patientRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public Patient setPatientNonNullFields(PatientDTO patientDTO, Patient patientToUpdate) {
        if (patientDTO.getLastPaidHealthInsurance() != null) {
            patientToUpdate.setLastPaidHealthInsurance(patientDTO.getLastPaidHealthInsurance());
        }

        personService.setPersonNonNullFields(patientDTO, patientToUpdate);
        return patientToUpdate;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public PatientDTO addGeneralPractitioner(Long patientId, Long generalPractitionerId) {
        Optional<Patient> optionalPatientToAddGeneralPractitioner = patientRepository.findById(patientId);
        if (optionalPatientToAddGeneralPractitioner.isPresent()) {
            Patient patientToSave = optionalPatientToAddGeneralPractitioner.get();
            GeneralPractitionerDTO generalPractitionerDTO = generalPractitionerService.getGeneralPractitioner(generalPractitionerId);
            patientToSave.setGeneralPractitioner(generalPractitionerMapper.toGeneralPractitioner(generalPractitionerDTO));
            return patientMapper.toPatientDTO(patientRepository.save(patientToSave));
        }

        throw new PatientNotFoundException("Such a patient not found", patientId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    public PatientDTO removeGeneralPractitioner(Long patientId) {
        Optional<Patient> optionalPatientToAddGeneralPractitioner = patientRepository.findById(patientId);
        if (optionalPatientToAddGeneralPractitioner.isPresent()) {
            Patient patientToSave = optionalPatientToAddGeneralPractitioner.get();
            patientToSave.setGeneralPractitioner(null);
            return patientMapper.toPatientDTO(patientRepository.save(patientToSave));
        }

        throw new PatientNotFoundException("Such a patient not found", patientId);
    }

    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN', 'PATIENT')")
    public User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}