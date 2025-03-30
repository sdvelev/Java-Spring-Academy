package com.digital.rewe.academy.medicalrecord.data.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;

import java.util.List;

public interface GeneralPractitionerService {

    // TODO Change for general practitioner and continue work
    List<GeneralPractitionerDTO> getGeneralPractitioners();
    GeneralPractitionerDTO getGeneralPractitioner(Long id);
    //    DoctorDTO createDoctor(DoctorCreateDTO doctorCreateDTO);
    GeneralPractitionerDTO createGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO);
    GeneralPractitionerDTO createGeneralPractitionerWithExistingAddresses(GeneralPractitionerDTO generalPractitionerDTO, Long permanentAddressId, Long practiceAddressId);
    GeneralPractitionerDTO updateGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO, Long id);
    void deleteGeneralPractitioner(Long id);
    GeneralPractitioner setGeneralPractitionerNonNullFields(GeneralPractitionerDTO generalPractitionerDTO, GeneralPractitioner generalPractitionerToUpdate);

}
