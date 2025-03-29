package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.data.entity.Person;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import com.digital.rewe.academy.medicalrecord.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<AddressDTO> getAddresses();
    AddressDTO getAddress(Long id);
    AddressDTO createAddress(AddressDTO addressDTO);
    AddressDTO updateAddress(AddressDTO addressDTO, Long id);
    void deleteAddress(Long id);
    Address setAddressNonNullFields(AddressDTO addressDTO, Address addressToUpdate);
}
