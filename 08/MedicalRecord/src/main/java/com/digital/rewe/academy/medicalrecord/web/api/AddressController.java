package com.digital.rewe.academy.medicalrecord.web.api;

import com.digital.rewe.academy.medicalrecord.data.service.AddressService;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressDTO> getAddresses() {
        return addressService.getAddresses();
    }

    @GetMapping("/{id}")
    public AddressDTO getAddress(@Valid @PathVariable @Positive Long id) {
        return addressService.getAddress(id);
    }

    @PostMapping
    public AddressDTO createAddress(@RequestBody @Valid AddressDTO addressDTO) {
        return addressService.createAddress(addressDTO);
    }

    @PutMapping("/{id}")
    public AddressDTO updateAddress(@RequestBody @Valid AddressDTO addressDTO, @PathVariable @Valid @Positive Long id) {
        return this.addressService.updateAddress(addressDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable @Valid @Positive Long id) {
        this.addressService.deleteAddress(id);
    }
}