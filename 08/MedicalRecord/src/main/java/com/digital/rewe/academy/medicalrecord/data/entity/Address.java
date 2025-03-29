package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "address")
@Getter
@Setter
public class Address extends BaseEntity {

    @Column(name = "populated_place")
    @NotBlank(message = "The populated place of an address cannot be null, empty or blank")
    private String populatedPlace;

    @Column(name = "country_name")
    @NotBlank(message = "The country name of an address cannot be null, empty or blank")
    private String countryName;

    @Column(name = "postal_code", length = 20)
    @NotBlank(message = "The postal code of an address cannot be null, empty or blank")
    private String postalCode;

    @Column(name = "region")
    @NotBlank(message = "The region of an address cannot be null, empty or blank")
    private String region;

    @Column(name = "municipality")
    @NotBlank(message = "The municipality of an address cannot be null, empty or blank")
    private String municipality;

    @Column(name = "street_name")
    @NotBlank(message = "The street name of an address cannot be null, empty or blank")
    private String streetName;

    @Column(name = "dwelling_number", length = 10)
    @NotBlank(message = "The dwelling number of an address cannot be null, empty or blank")
    private String dwellingNumber;

    @Column(name = "district")
    private String district;

    @Column(name = "entrance", length = 10)
    private String entrance;

    @Column(name = "floor_number", length = 10)
    private String floorNumber;

    @Column(name = "apartment_number", length = 10)
    private String apartmentNumber;

    @OneToMany(mappedBy = "permanentAddress")
    private Set<Person> personOnAddressSet;

    @OneToMany(mappedBy = "practiceAddress")
    private Set<GeneralPractitioner> generalPractitionerOnAddressSet;
}