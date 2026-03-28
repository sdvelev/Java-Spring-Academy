package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO extends BaseEntityDTO {

    @JsonProperty("populatedPlace")
    @NotBlank(message = "The populated place of an address cannot be null, empty or blank")
    private String populatedPlace;

    @JsonProperty("countryName")
    @NotBlank(message = "The country name of an address cannot be null, empty or blank")
    private String countryName;

    @JsonProperty("postalCode")
    @NotBlank(message = "The postal code of an address cannot be null, empty or blank")
    private String postalCode;

    @JsonProperty("region")
    @NotBlank(message = "The region of an address cannot be null, empty or blank")
    private String region;

    @JsonProperty("municipality")
    @NotBlank(message = "The municipality of an address cannot be null, empty or blank")
    private String municipality;

    @JsonProperty("streetName")
    @NotBlank(message = "The street name of an address cannot be null, empty or blank")
    private String streetName;

    @JsonProperty("dwellingNumber")
    @NotBlank(message = "The dwelling number of an address cannot be null, empty or blank")
    private String dwellingNumber;

    @JsonProperty("district")
    private String district;

    @JsonProperty("entrance")
    private String entrance;

    @JsonProperty("floorNumber")
    private String floorNumber;

    @JsonProperty("apartmentNumber")
    private String apartmentNumber;
}