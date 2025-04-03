package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getAddresses();

    AddressDTO getAddress(Long id);

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(AddressDTO addressDTO, Long id);

    Address setAddressNonNullFields(AddressDTO addressDTO, Address addressToUpdate);

    void deleteAddress(Long id);
}