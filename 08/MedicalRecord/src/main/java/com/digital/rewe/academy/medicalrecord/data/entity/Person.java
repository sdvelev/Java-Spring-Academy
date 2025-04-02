package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "person")
@Getter
@Setter
public class Person extends User {

    @Column(name = "first_name")
    @NotBlank(message = "The first name of a person cannot be null, empty or blank")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    @NotBlank(message = "The last name of a person cannot be null, empty or blank")
    private String lastName;

    @Column(name = "personal_id_number", length = 20, unique = true)
    @NotBlank(message = "The personal ID number of a person cannot be null, empty or blank")
    private String personalIdNumber;

    @Column(name = "birthdate")
    @NotNull(message = "The birthdate of a person cannot be null")
    @PastOrPresent(message = "The birthdate of a person cannot be in the future")
    private LocalDate birthDate;

    @Column(name = "phone_number", length = 20)
    @NotBlank(message = "The phone number of a person cannot be null, empty or blank")
    private String phoneNumber;

    @Column(name = "email", length = 50)
    @Email(message = "The email of a person must be valid")
    private String email;

//    @Column(name = "username", length = 50)
//    private String username;
//
//    @Column(name = "password", length = 50)
//    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address permanentAddress;
}