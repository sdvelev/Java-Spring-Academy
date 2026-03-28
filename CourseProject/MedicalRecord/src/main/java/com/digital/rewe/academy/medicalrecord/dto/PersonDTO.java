package com.digital.rewe.academy.medicalrecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO extends BaseEntityDTO {

    @JsonProperty("firstName")
    @NotBlank(message = "The first name of a person cannot be null, empty or blank")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    @NotBlank(message = "The last name of a person cannot be null, empty or blank")
    private String lastName;

    @JsonProperty("birthdate")
    @NotNull(message = "The birthdate of a person cannot be null")
    @PastOrPresent(message = "The birthdate of a person cannot be in the future")
    private LocalDate birthDate;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "The phone number of a person cannot be null, empty or blank")
    private String phoneNumber;

    @JsonProperty("email")
    @Email(message = "The email of a person must be valid")
    private String email;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("permanentAddressDTO")
    private AddressDTO permanentAddressDTO;
}