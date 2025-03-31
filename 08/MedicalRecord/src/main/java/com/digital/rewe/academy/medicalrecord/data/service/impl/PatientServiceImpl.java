package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.data.repo.PatientRepository;
import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.data.service.GeneralPractitionerService;
import com.digital.rewe.academy.medicalrecord.data.service.PatientService;
import com.digital.rewe.academy.medicalrecord.data.service.PersonService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import com.digital.rewe.academy.medicalrecord.mapper.GeneralPractitionerMapper;
import com.digital.rewe.academy.medicalrecord.mapper.PatientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    private final PersonService personService;

    private final GeneralPractitionerService generalPractitionerService;
    private final GeneralPractitionerMapper generalPractitionerMapper;

    @Override
    public List<PatientDTO> getPatients() {
        return patientMapper.toPatientDTOCollection(patientRepository.findAll());
    }

    @Override
    public List<PatientDTO> getPatientsWithSpecificDiagnose(Long diagnoseId) {
        return patientMapper.toPatientDTOCollection(patientRepository.findDistinctByMedicalvisitSet_DiagnoseSet_Id(diagnoseId));
    }

    @Override
    public List<PatientDTO> getPatientsWithGeneralPractitioner(Long generalPractitionerId) {
        return patientMapper.toPatientDTOCollection(patientRepository.findByGeneralPractitionerId(generalPractitionerId));
    }

    @Override
    public Long getCountByGeneralPractitionerId(Long generalPractitionerId) {
        return patientRepository.countByGeneralPractitionerId(generalPractitionerId);
    }

    @Override
    public PatientDTO getPatient(Long id) {
        return patientMapper.toPatientDTO(patientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Patient with id " + id + " not found")));
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patientToSave = new Patient();
        patientToSave = setPatientNonNullFields(patientDTO, patientToSave);

        AddressDTO addressToSave = addressService.createAddress(patientDTO.getPermanentAddressDTO());
        patientToSave.setPermanentAddress(addressMapper.toAddress(addressToSave));

        return patientMapper.toPatientDTO(patientRepository.save(patientToSave));
    }

    @Override
    public PatientDTO createPatientWithExistingAddress(PatientDTO patientDTO, Long addressId) {
        Patient patientToSave = new Patient();
        patientToSave = setPatientNonNullFields(patientDTO, patientToSave);

        patientToSave.setPermanentAddress(addressMapper.toAddress(addressService.getAddress(addressId)));

        return patientMapper.toPatientDTO(patientRepository.save(patientToSave));
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO, Long id) {
        Optional<Patient> optionalPatientToUpdate = patientRepository.findById(id);
        if (optionalPatientToUpdate.isPresent()) {
            Patient patientToUpdate = setPatientNonNullFields(patientDTO, optionalPatientToUpdate.get());
            return patientMapper.toPatientDTO(patientRepository.save(patientToUpdate));
        }

        throw new EntityNotFoundException("Doctor with id " + id + " not found");
    }

    @Override
    public void deletePatient(Long id) {
        this.patientRepository.deleteById(id);
    }

    @Override
    public Patient setPatientNonNullFields(PatientDTO patientDTO, Patient patientToUpdate) {
        if (patientDTO.getLastPaidHealthInsurance() != null) {
            patientToUpdate.setLastPaidHealthInsurance(patientDTO.getLastPaidHealthInsurance());
        }

        personService.setPersonNonNullFields(patientDTO, patientToUpdate);
        return patientToUpdate;
    }

    @Override
    public PatientDTO addGeneralPractitioner(Long patientId, Long generalPractitionerId) {
        Optional<Patient> optionalPatientToAddGeneralPractitioner = patientRepository.findById(patientId);
        if (optionalPatientToAddGeneralPractitioner.isPresent()) {
            Patient patientToSave = optionalPatientToAddGeneralPractitioner.get();
            GeneralPractitionerDTO generalPractitionerDTO = generalPractitionerService.getGeneralPractitioner(generalPractitionerId);
            patientToSave.setGeneralPractitioner(generalPractitionerMapper.toGeneralPractitioner(generalPractitionerDTO));
            return patientMapper.toPatientDTO(patientRepository.save(patientToSave));
        }

        throw new EntityNotFoundException("Patient with id " + patientId + " not found");
    }

    @Override
    public PatientDTO removeGeneralPractitioner(Long patientId) {
        Optional<Patient> optionalPatientToAddGeneralPractitioner = patientRepository.findById(patientId);
        if (optionalPatientToAddGeneralPractitioner.isPresent()) {
            Patient patientToSave = optionalPatientToAddGeneralPractitioner.get();
            patientToSave.setGeneralPractitioner(null);
            return patientMapper.toPatientDTO(patientRepository.save(patientToSave));
        }

        throw new EntityNotFoundException("Patient with id " + patientId + " not found");
    }
}