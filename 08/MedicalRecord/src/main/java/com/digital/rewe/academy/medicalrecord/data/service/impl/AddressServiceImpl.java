package com.digital.rewe.academy.medicalrecord.data.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.repo.AddressRepository;
import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.mapper.AddressMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressDTO> getAddresses() {
        return addressMapper.toAddressDTOCollection(addressRepository.findAll());
    }

    @Override
    public AddressDTO getAddress(Long id) {
        return addressMapper.toAddressDTO(addressRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Address with id: " + id + " not found")));
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        return addressMapper.toAddressDTO(addressRepository.save(addressMapper.toAddress(addressDTO)));
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long id) {
        Optional<Address> optionalAddressToUpdate = addressRepository.findById(id);
        if (optionalAddressToUpdate.isPresent()) {
            Address addressToUpdate = setAddressNonNullFields(addressDTO, optionalAddressToUpdate.get());
            return addressMapper.toAddressDTO(addressRepository.save(addressToUpdate));
        }

        throw new EntityNotFoundException("Address with id " + id + " not found");
    }

    @Override
    public Address setAddressNonNullFields(AddressDTO addressDTO, Address addressToUpdate) {
        if (addressDTO.getPopulatedPlace() != null) {
            addressToUpdate.setPopulatedPlace(addressDTO.getPopulatedPlace());
        }

        if (addressDTO.getCountryName() != null) {
            addressToUpdate.setCountryName(addressDTO.getCountryName());
        }

        if (addressDTO.getPostalCode() != null) {
            addressToUpdate.setPostalCode(addressDTO.getPostalCode());
        }

        if (addressDTO.getRegion() != null) {
            addressToUpdate.setRegion(addressDTO.getRegion());
        }

        if (addressDTO.getMunicipality() != null) {
            addressToUpdate.setMunicipality(addressDTO.getMunicipality());
        }

        if (addressDTO.getStreetName() != null) {
            addressToUpdate.setStreetName(addressDTO.getStreetName());
        }

        if (addressDTO.getDwellingNumber() != null) {
            addressToUpdate.setDwellingNumber(addressDTO.getDwellingNumber());
        }

        if (addressDTO.getDistrict() != null) {
            addressToUpdate.setDistrict(addressToUpdate.getDistrict());
        }

        if (addressDTO.getEntrance() != null) {
            addressToUpdate.setEntrance(addressToUpdate.getEntrance());
        }

        if (addressDTO.getFloorNumber() != null) {
            addressToUpdate.setFloorNumber(addressDTO.getFloorNumber());
        }

        if (addressDTO.getApartmentNumber() != null) {
            addressToUpdate.setApartmentNumber(addressDTO.getApartmentNumber());
        }

        return addressToUpdate;
    }

    @Override
    public void deleteAddress(Long id) {
        this.addressRepository.deleteById(id);
    }
}