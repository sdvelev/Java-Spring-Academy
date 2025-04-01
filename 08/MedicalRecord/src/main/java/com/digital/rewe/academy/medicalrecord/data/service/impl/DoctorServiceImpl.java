package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.repo.DoctorRepository;
import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.data.service.DoctorService;
import com.digital.rewe.academy.medicalrecord.data.service.PersonService;
import com.digital.rewe.academy.medicalrecord.data.service.SpecialtyService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.SpecialtyDTO;
import com.digital.rewe.academy.medicalrecord.exception.DoctorNotFoundException;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import com.digital.rewe.academy.medicalrecord.mapper.DoctorMapper;
import com.digital.rewe.academy.medicalrecord.mapper.SpecialtyMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
            .orElseThrow(() -> new DoctorNotFoundException("Such a doctor not found", id)));
    }

    @Override
    public List<DoctorDTO> getDoctorWithMostSickLeaves() {
        return doctorMapper.toDoctorDTOCollection(doctorRepository.findDoctorWithMostSickLeaves());
    }

    @Override
    @Transactional
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctorToSave = new Doctor();
        doctorToSave = setDoctorNonNullFields(doctorDTO, doctorToSave);

        AddressDTO addressToSave = addressService.createAddress(doctorDTO.getPermanentAddressDTO());
        doctorToSave.setPermanentAddress(addressMapper.toAddress(addressToSave));
        return doctorMapper.toDoctorDTO(doctorRepository.save(doctorToSave));
    }

    @Override
    public DoctorDTO createDoctorWithExistingAddress(DoctorDTO doctorDTO, Long addressId) {
        Doctor doctorToSave = new Doctor();
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

        throw new DoctorNotFoundException("Such a doctor not found", id);
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

        throw new DoctorNotFoundException("Such a doctor not found", doctorId);
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

        throw new DoctorNotFoundException("Such a doctor not found", doctorId);
    }
}