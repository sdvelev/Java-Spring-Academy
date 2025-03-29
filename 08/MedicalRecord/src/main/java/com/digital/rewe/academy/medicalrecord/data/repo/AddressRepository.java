package com.digital.rewe.academy.medicalrecord.data.repo;

import com.digital.rewe.academy.medicalrecord.data.entity.Address;
import com.digital.rewe.academy.medicalrecord.dto.AddressDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByPopulatedPlaceAndCountryNameAndPostalCodeAndRegionAndMunicipalityAndStreetNameAndDwellingNumberAndDistrictAndEntranceAndFloorNumberAndApartmentNumber(
        @NotBlank(message = "The populated place of an address cannot be null, empty or blank") String populatedPlace,
        @NotBlank(message = "The country name of an address cannot be null, empty or blank") String countryName,
        @NotBlank(message = "The postal code of an address cannot be null, empty or blank") String postalCode,
        @NotBlank(message = "The region of an address cannot be null, empty or blank") String region,
        @NotBlank(message = "The municipality of an address cannot be null, empty or blank") String municipality,
        @NotBlank(message = "The street name of an address cannot be null, empty or blank") String streetName,
        @NotBlank(message = "The dwelling number of an address cannot be null, empty or blank") String dwellingNumber,
        String district, String entrance, String floorNumber, String apartmentNumber);
}