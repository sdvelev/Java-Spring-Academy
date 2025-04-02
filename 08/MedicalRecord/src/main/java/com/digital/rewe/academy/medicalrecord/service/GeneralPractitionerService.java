package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.GeneralPractitioner;
import com.digital.rewe.academy.medicalrecord.dto.GeneralPractitionerDTO;

import java.util.List;

public interface GeneralPractitionerService {
    List<GeneralPractitionerDTO> getGeneralPractitioners();

    GeneralPractitionerDTO getGeneralPractitioner(Long id);

    GeneralPractitionerDTO createGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO);

    GeneralPractitionerDTO createGeneralPractitionerWithExistingAddresses(GeneralPractitionerDTO generalPractitionerDTO,
                                                                          Long permanentAddressId,
                                                                          Long practiceAddressId);

    GeneralPractitionerDTO updateGeneralPractitioner(GeneralPractitionerDTO generalPractitionerDTO, Long id);

    GeneralPractitioner setGeneralPractitionerNonNullFields(GeneralPractitionerDTO generalPractitionerDTO,
                                                            GeneralPractitioner generalPractitionerToUpdate);

    void deleteGeneralPractitioner(Long id);
}