package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Diagnose;
import com.digital.rewe.academy.medicalrecord.data.entity.Doctor;
import com.digital.rewe.academy.medicalrecord.data.entity.MedicalVisit;
import com.digital.rewe.academy.medicalrecord.data.entity.Patient;
import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.entity.Treatment;
import com.digital.rewe.academy.medicalrecord.data.entity.User;
import com.digital.rewe.academy.medicalrecord.data.repo.MedicalVisitRepository;
import com.digital.rewe.academy.medicalrecord.dto.DiagnoseDTO;
import com.digital.rewe.academy.medicalrecord.dto.DoctorDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitCreateDTO;
import com.digital.rewe.academy.medicalrecord.dto.MedicalVisitDTO;
import com.digital.rewe.academy.medicalrecord.dto.PatientDTO;
import com.digital.rewe.academy.medicalrecord.dto.TreatmentDTO;
import com.digital.rewe.academy.medicalrecord.exception.MedicalVisitNotFoundException;
import com.digital.rewe.academy.medicalrecord.service.impl.MedicalVisitServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class MedicalVisitServiceTest {

    @MockitoBean
    private MedicalVisitRepository medicalVisitRepository;

    @Autowired
    private MedicalVisitServiceImpl medicalVisitService;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private DiagnoseService diagnoseService;

    @MockitoBean
    private TreatmentService treatmentService;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsEmptyList() {
        Mockito.when(medicalVisitRepository.findAll()).thenReturn(List.of());

        assertEquals(0, medicalVisitService.getMedicalVisits().size(),
            "Medical visit list is expected to be empty but it is not");
        assertDoesNotThrow(() -> medicalVisitService.getMedicalVisits(),
            "No exception is expected to be thrown");
    }

    @Test
    void testGetMedicalVisitsAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
            medicalVisitService.getMedicalVisits(),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findAll();
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testGetMedicalVisitsAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisits(),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findAll();
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testGetMedicalVisitsAccessDeniedExceptionFromDoctor() {
        Mockito.when(medicalVisitRepository.findAll()).thenReturn(List.of());

        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisits(),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findAll();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsSuccessfully() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findAll()).thenReturn(List.of(firstMedicalVisit));
        List<MedicalVisitDTO> result = medicalVisitService.getMedicalVisits();

        assertEquals(1, result.size(),
            "Medical visit list is expected to be 1 but it is not");
        assertEquals(1L, (Long) result.get(0).getId(),
            "The actual id of medical visit is not the same as the expected");
        assertEquals(LocalDate.of(2025, 4, 3), result.get(0).getDateOfVisit(),
            "The actual date of medical visit is not the same as the expected");
        assertEquals(1L, result.get(0).getDoctorDTO().getId(),
            "The actual doctor id of medical visit is not the same as the expected");
        assertEquals(2L, result.get(0).getPatientDTO().getId(),
            "The actual patient id of medical visit is not the same as the expected");
        assertEquals("John", result.get(0).getDoctorDTO().getFirstName(),
            "The first name of doctor of medical visit is not the same as the expected");
    }

    @Test
    void testGetMedicalVisitByAdminSuccessfully() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setTreatmentSet(new HashSet<>());
        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAuthorities(Set.of(new Role(1, "ADMIN", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MedicalVisitDTO result = medicalVisitService.getMedicalVisit(1L);

        assertNotNull(result, "The expected medical visit cannot be null");
        assertEquals(1L, result.getId(),
            "The actual id of medical visit is not the same as the expected");
        assertEquals(LocalDate.of(2025, 4, 3), result.getDateOfVisit(),
            "The actual date of medical visit is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The actual doctor id of medical visit is not the same as the expected");
        assertEquals(2L, result.getPatientDTO().getId(),
            "The actual patient id of medical visit is not the same as the expected");
    }

    @Test
    void testGetMedicalVisitByDoctorSuccessfully() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MedicalVisitDTO result = medicalVisitService.getMedicalVisit(1L);

        assertNotNull(result, "The expected medical visit cannot be null");
        assertEquals(1L, result.getId(),
            "The actual id of medical visit is not the same as the expected");
        assertEquals(LocalDate.of(2025, 4, 3), result.getDateOfVisit(),
            "The actual date of medical visit is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The actual doctor id of medical visit is not the same as the expected");
        assertEquals(2L, result.getPatientDTO().getId(),
            "The actual patient id of medical visit is not the same as the expected");
    }

    @Test
    void testGetMedicalVisitByAnotherDoctorMedicalVisitNotFoundException() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(3L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(MedicalVisitNotFoundException.class, () -> medicalVisitService.getMedicalVisit(1L),
            "MedicalVisitNotFoundException is expected but not thrown");
    }

    @Test
    void testGetMedicalVisitByPatientSuccessfully() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(2L);
        user.setUsername("patient");
        user.setPassword("patient");
        user.setAuthorities(Set.of(new Role(1, "PATIENT", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("PATIENT")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MedicalVisitDTO result = medicalVisitService.getMedicalVisit(1L);

        assertNotNull(result, "The expected medical visit cannot be null");
        assertEquals(1L, result.getId(),
            "The actual id of medical visit is not the same as the expected");
        assertEquals(LocalDate.of(2025, 4, 3), result.getDateOfVisit(),
            "The actual date of medical visit is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The actual doctor id of medical visit is not the same as the expected");
        assertEquals(2L, result.getPatientDTO().getId(),
            "The actual patient id of medical visit is not the same as the expected");
    }

    @Test
    void testGetMedicalVisitByAnotherPatientMedicalVisitNotFoundException() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(3L);
        user.setUsername("patient");
        user.setPassword("patient");
        user.setAuthorities(Set.of(new Role(1, "PATIENT", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("PATIENT")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(MedicalVisitNotFoundException.class, () -> medicalVisitService.getMedicalVisit(1L),
            "MedicalVisitNotFoundException is expected but not thrown");
    }

    @Test
    void testGetMedicalVisitAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.getMedicalVisit(6L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findById(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsCountByDoctorIdZeroAnswer() {
        Mockito.when(medicalVisitRepository.countByDoctorId(any())).thenReturn(0L);
        assertEquals(0L, medicalVisitService.getMedicalVisitsCountByDoctorId(66L),
            "Zero is expected but not returned");
        assertDoesNotThrow(() -> medicalVisitService.getMedicalVisitsCountByDoctorId(66L),
            "No exception is expected to be thrown");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsCountByDoctorIdPositiveAnswer() {
        Mockito.when(medicalVisitRepository.countByDoctorId(any())).thenReturn(6L);
        assertEquals(6L, medicalVisitService.getMedicalVisitsCountByDoctorId(6L),
            "Positive number is expected but not returned");
        assertDoesNotThrow(() -> medicalVisitService.getMedicalVisitsCountByDoctorId(6L),
            "No exception is expected to be thrown");
    }

    @Test
    void testGetMedicalVisitsCountByDoctorIdAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.getMedicalVisitsCountByDoctorId(6L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).countByDoctorId(any());
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testGetMedicalVisitsCountByDoctorIdAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisitsCountByDoctorId(6L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).countByDoctorId(any());
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testGetMedicalVisitsCountByDoctorIdAccessDeniedExceptionFromDoctor() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisitsCountByDoctorId(6L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).countByDoctorId(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsByPatientIdNoVisits() {
        Mockito.when(medicalVisitRepository.findByPatientId(any())).thenReturn(List.of());
        assertTrue(medicalVisitService.getMedicalVisitsByPatientId(6L).isEmpty(),
            "Result list is expected to be empty but is not");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsByPatientIdSuccessfully() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findByPatientId(2L)).thenReturn(List.of(firstMedicalVisit));
        List<MedicalVisitDTO> result = medicalVisitService.getMedicalVisitsByPatientId(2L);

        assertEquals(1, result.size(),
            "Medical visit list is expected to be 1 but it is not");
        assertEquals(1L, (Long) result.get(0).getId(),
            "The actual id of medical visit is not the same as the expected");
        assertEquals(LocalDate.of(2025, 4, 3), result.get(0).getDateOfVisit(),
            "The actual date of medical visit is not the same as the expected");
        assertEquals(1L, result.get(0).getDoctorDTO().getId(),
            "The actual doctor id of medical visit is not the same as the expected");
        assertEquals(2L, result.get(0).getPatientDTO().getId(),
            "The actual patient id of medical visit is not the same as the expected");
        assertEquals("John", result.get(0).getDoctorDTO().getFirstName(),
            "The first name of doctor of medical visit is not the same as the expected");
    }

    @Test
    void testGetMedicalVisitsByPatientIdAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.getMedicalVisitsByPatientId(6L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findByPatientId(any());
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testGetMedicalVisitsByPatientIdAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisitsByPatientId(6L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findByPatientId(any());
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testGetMedicalVisitsByPatientIdAccessDeniedExceptionFromDoctor() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisitsByPatientId(6L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findByPatientId(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsByPeriodNoVisits() {
        Mockito.when(medicalVisitRepository.findByDateOfVisitBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of());
        assertTrue(medicalVisitService.getMedicalVisitsByPeriod(LocalDate.now(), LocalDate.now().plusDays(1)).isEmpty(),
            "Result list is expected to be empty but is not");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitsGetMedicalVisitsByPeriodSuccessfully() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findByDateOfVisitBetween(LocalDate.of(2025, 4, 1),
            LocalDate.of(2025, 4, 10))).thenReturn(List.of(firstMedicalVisit));
        List<MedicalVisitDTO> result = medicalVisitService.getMedicalVisitsByPeriod(LocalDate.of(2025, 4, 1),
            LocalDate.of(2025, 4, 10));

        assertEquals(1, result.size(),
            "Medical visit list is expected to be 1 but it is not");
        assertEquals(1L, (Long) result.get(0).getId(),
            "The actual id of medical visit is not the same as the expected");
        assertEquals(LocalDate.of(2025, 4, 3), result.get(0).getDateOfVisit(),
            "The actual date of medical visit is not the same as the expected");
        assertEquals(1L, result.get(0).getDoctorDTO().getId(),
            "The actual doctor id of medical visit is not the same as the expected");
        assertEquals(2L, result.get(0).getPatientDTO().getId(),
            "The actual patient id of medical visit is not the same as the expected");
        assertEquals("John", result.get(0).getDoctorDTO().getFirstName(),
            "The first name of doctor of medical visit is not the same as the expected");
    }

    @Test
    void testGetMedicalVisitsByPeriodAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.getMedicalVisitsByPeriod(LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 10)),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findByDateOfVisitBetween(LocalDate.of(2025, 4, 1),
            LocalDate.of(2025, 4, 10));
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testGetMedicalVisitsGetMedicalVisitsByPeriodAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisitsByPeriod(LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 10)),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findByDateOfVisitBetween(LocalDate.of(2025, 4, 1),
            LocalDate.of(2025, 4, 10));
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testGetMedicalVisitsGetMedicalVisitsByPeriodAccessDeniedExceptionFromDoctor() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.getMedicalVisitsByPeriod(LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 10)),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).findByDateOfVisitBetween(LocalDate.of(2025, 4, 1),
            LocalDate.of(2025, 4, 10));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testCreateMedicalServiceByAdminSuccessfully() {
        LocalDate dateOfVisit = LocalDate.of(2025, 4, 3);
        MedicalVisitCreateDTO medicalVisitCreateDTO = new MedicalVisitCreateDTO(dateOfVisit, 1L, 2L);

        Mockito.when(patientService.getPatient(2L)).thenReturn(new PatientDTO());
        Mockito.when(doctorService.getDoctor(1L)).thenReturn(new DoctorDTO());

        MedicalVisit medicalVisitToReturn = new MedicalVisit();
        medicalVisitToReturn.setDateOfVisit(dateOfVisit);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(medicalVisitToReturn);

        MedicalVisitDTO result = medicalVisitService.createMedicalVisit(medicalVisitCreateDTO);
        assertNotNull(result, "The expected result is cannot be null");
        assertEquals(dateOfVisit, result.getDateOfVisit(),
            "The actual date of medical visit after creation is not the same as the expected");
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testCreateMedicalServiceByDoctorSuccessfully() {
        LocalDate dateOfVisit = LocalDate.of(2025, 4, 3);
        MedicalVisitCreateDTO medicalVisitCreateDTO = new MedicalVisitCreateDTO(dateOfVisit, 1L, 2L);

        Mockito.when(patientService.getPatient(2L)).thenReturn(new PatientDTO());
        Mockito.when(doctorService.getDoctor(1L)).thenReturn(new DoctorDTO());

        MedicalVisit medicalVisitToReturn = new MedicalVisit();
        medicalVisitToReturn.setDateOfVisit(dateOfVisit);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(medicalVisitToReturn);

        MedicalVisitDTO result = medicalVisitService.createMedicalVisit(medicalVisitCreateDTO);
        assertNotNull(result, "The expected result is cannot be null");
        assertEquals(dateOfVisit, result.getDateOfVisit(),
            "The actual date of medical visit after creation is not the same as the expected");
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testCreateMedicalServiceAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.createMedicalVisit(new MedicalVisitCreateDTO()),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testCreateMedicalVisitAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.createMedicalVisit(new MedicalVisitCreateDTO()),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testDeleteMedicalVisitSuccessfully() {
        Mockito.doNothing().when(medicalVisitRepository).delete(any());
        medicalVisitService.deleteMedicalVisit(6L);
        Mockito.verify(medicalVisitRepository).deleteById(6L);
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testDeleteMedicalVisitAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.deleteMedicalVisit(1L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).deleteById(any());
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testDeleteMedicalVisitAccessDeniedExceptionFromDoctor() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.deleteMedicalVisit(1L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).deleteById(any());
    }

    @Test
    void testDeleteMedicalVisitAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.deleteMedicalVisit(1L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).deleteById(any());
    }

    @Test
    void testAddDiagnoseSuccessfullyByDoctor() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setDiagnoseSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DiagnoseDTO diagnoseDTO = new DiagnoseDTO("123456", "diagnose_description");
        Mockito.when(diagnoseService.getDiagnose(any())).thenReturn(diagnoseDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.addDiagnose(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(1, result.getDiagnoseDTOSet().size(),
            "The size of the diagnose set is expected to be positive");
        assertEquals("123456", result.getDiagnoseDTOSet().iterator().next().getIcdCode(),
            "The icd code of the added diagnose is not the same as the expected");
        assertEquals("diagnose_description", result.getDiagnoseDTOSet().iterator().next().getDiagnoseDescription(),
            "The description of the added diagnose is not the same as the expected");
    }

    @Test
    void testAddDiagnoseByAnotherDoctorMedicalVisitNotFoundException() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(4L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setDiagnoseSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(MedicalVisitNotFoundException.class, () -> medicalVisitService.addDiagnose(1L, 1L),
            "MedicalVisitNotFoundException is expected but not thrown");
        Mockito.verify(diagnoseService, Mockito.never()).getDiagnose(any());
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testAddDiagnoseSuccessfullyByAdmin() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setDiagnoseSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAuthorities(Set.of(new Role(1, "ADMIN", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DiagnoseDTO diagnoseDTO = new DiagnoseDTO("123456", "diagnose_description");
        Mockito.when(diagnoseService.getDiagnose(any())).thenReturn(diagnoseDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.addDiagnose(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(1, result.getDiagnoseDTOSet().size(),
            "The size of the diagnose set is expected to be positive");
        assertEquals("123456", result.getDiagnoseDTOSet().iterator().next().getIcdCode(),
            "The icd code of the added diagnose is not the same as the expected");
        assertEquals("diagnose_description", result.getDiagnoseDTOSet().iterator().next().getDiagnoseDescription(),
            "The description of the added diagnose is not the same as the expected");
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testAddDiagnoseAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.addDiagnose(1L, 1L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testAddDiagnoseAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.addDiagnose(1L, 1L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testRemoveDiagnoseSuccessfullyByDoctor() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DiagnoseDTO diagnoseDTO = new DiagnoseDTO("123456", "diagnose_description");
                Diagnose diagnose = new Diagnose();
                diagnose.setIcdCode("123456");
                diagnose.setDiagnoseDescription("diagnose_description");
                Set<Diagnose> diagnoseSetToAdd = new HashSet<>();
                diagnoseSetToAdd.add(diagnose);
                firstMedicalVisit.setDiagnoseSet(diagnoseSetToAdd);

        Mockito.when(diagnoseService.getDiagnose(any())).thenReturn(diagnoseDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.removeDiagnose(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(0, result.getDiagnoseDTOSet().size(),
            "The size of the diagnose set is expected to be empty");
    }

    @Test
    void testRemoveDiagnoseByAnotherDoctorMedicalVisitNotFoundException() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(4L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setDiagnoseSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(MedicalVisitNotFoundException.class, () -> medicalVisitService.removeDiagnose(1L, 1L),
            "MedicalVisitNotFoundException is expected but not thrown");
        Mockito.verify(diagnoseService, Mockito.never()).getDiagnose(any());
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testRemoveDiagnoseSuccessfullyByAdmin() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAuthorities(Set.of(new Role(1, "ADMIN", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DiagnoseDTO diagnoseDTO = new DiagnoseDTO("123456", "diagnose_description");
        Diagnose diagnose = new Diagnose();
        diagnose.setIcdCode("123456");
        diagnose.setDiagnoseDescription("diagnose_description");
        Set<Diagnose> diagnoseSetToAdd = new HashSet<>();
        diagnoseSetToAdd.add(diagnose);
        firstMedicalVisit.setDiagnoseSet(diagnoseSetToAdd);

        Mockito.when(diagnoseService.getDiagnose(any())).thenReturn(diagnoseDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.removeDiagnose(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(0, result.getDiagnoseDTOSet().size(),
            "The size of the diagnose set is expected to be empty");
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testRemoveDiagnoseAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.removeDiagnose(1L, 1L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testRemoveDiagnoseAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.removeDiagnose(1L, 1L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testAddTreatmentSuccessfullyByDoctor() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TreatmentDTO treatmentDTO = new TreatmentDTO("treatment_name", "treatment_description");
        Mockito.when(treatmentService.getTreatment(any())).thenReturn(treatmentDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.addTreatment(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(1, result.getTreatmentDTOSet().size(),
            "The size of the treatment set is expected to be positive");
        assertEquals("treatment_name", result.getTreatmentDTOSet().iterator().next().getName(),
            "The name of the added treatment is not the same as the expected");
        assertEquals("treatment_description", result.getTreatmentDTOSet().iterator().next().getDescription(),
            "The description of the added treatment is not the same as the expected");
    }

    @Test
    void testAddTreatmentByAnotherDoctorMedicalVisitNotFoundException() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(4L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(MedicalVisitNotFoundException.class, () -> medicalVisitService.addTreatment(1L, 1L),
            "MedicalVisitNotFoundException is expected but not thrown");
        Mockito.verify(treatmentService, Mockito.never()).getTreatment(any());
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testAddTreatmentSuccessfullyByAdmin() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAuthorities(Set.of(new Role(1, "ADMIN", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TreatmentDTO treatmentDTO = new TreatmentDTO("treatment_name", "treatment_description");
        Mockito.when(treatmentService.getTreatment(any())).thenReturn(treatmentDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.addTreatment(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(1, result.getTreatmentDTOSet().size(),
            "The size of the treatment set is expected to be positive");
        assertEquals("treatment_name", result.getTreatmentDTOSet().iterator().next().getName(),
            "The name of the added treatment is not the same as the expected");
        assertEquals("treatment_description", result.getTreatmentDTOSet().iterator().next().getDescription(),
            "The description of the added treatment is not the same as the expected");
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testAddTreatmentAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.addTreatment(1L, 1L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testAddTreatmentAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.addTreatment(1L, 1L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testRemoveTreatmentSuccessfullyByDoctor() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TreatmentDTO treatmentDTO = new TreatmentDTO("treatment_name", "treatment_description");
        Treatment treatment = new Treatment();
        treatment.setName("treatment_name");
        treatment.setDescription("treatment_description");
        Set<Treatment> treatmentSetToAdd = new HashSet<>();
        treatmentSetToAdd.add(treatment);
        firstMedicalVisit.setTreatmentSet(treatmentSetToAdd);

        Mockito.when(treatmentService.getTreatment(any())).thenReturn(treatmentDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.removeTreatment(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(0, result.getTreatmentDTOSet().size(),
            "The size of the treatment set is expected to be empty");
    }

    @Test
    void testRemoveTreatmentByAnotherDoctorMedicalVisitNotFoundException() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(4L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);
        firstMedicalVisit.setTreatmentSet(new HashSet<>());

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("doctor");
        user.setPassword("doctor");
        user.setAuthorities(Set.of(new Role(1, "DOCTOR", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("DOCTOR")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(MedicalVisitNotFoundException.class, () -> medicalVisitService.removeTreatment(1L, 1L),
            "MedicalVisitNotFoundException is expected but not thrown");
        Mockito.verify(treatmentService, Mockito.never()).getTreatment(any());
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testRemoveTreatmentSuccessfullyByAdmin() {
        MedicalVisit firstMedicalVisit = new MedicalVisit();
        firstMedicalVisit.setId(1L);
        firstMedicalVisit.setDateOfVisit(LocalDate.of(2025, 4, 3));
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setFirstName("Alice");
        patient.setLastName("Jones");

        firstMedicalVisit.setDoctor(doctor);
        firstMedicalVisit.setPatient(patient);

        Mockito.when(medicalVisitRepository.findById(any())).thenReturn(Optional.of(firstMedicalVisit));

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAuthorities(Set.of(new Role(1, "ADMIN", Set.of())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TreatmentDTO treatmentDTO = new TreatmentDTO("treatment_name", "treatment_description");
        Treatment treatment = new Treatment();
        treatment.setName("treatment_name");
        treatment.setDescription("treatment_description");
        Set<Treatment> treatmentSetToAdd = new HashSet<>();
        treatmentSetToAdd.add(treatment);
        firstMedicalVisit.setTreatmentSet(treatmentSetToAdd);

        Mockito.when(treatmentService.getTreatment(any())).thenReturn(treatmentDTO);
        Mockito.when(medicalVisitRepository.save(any())).thenReturn(firstMedicalVisit);

        MedicalVisitDTO result = medicalVisitService.removeTreatment(1L, 1L);

        assertNotNull(result, "The result is not expected to be null");
        assertEquals(1L, result.getId(), "The id of the result is not the same as the expected");
        assertEquals(1L, result.getDoctorDTO().getId(),
            "The id of the doctor of the medical visit is not the same as the expected");
        assertEquals(0, result.getTreatmentDTOSet().size(),
            "The size of the treatment set is expected to be empty");
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testRemoveTreatmentAccessDeniedExceptionFromPatient() {
        assertThrows(AccessDeniedException.class, () ->
                medicalVisitService.removeTreatment(1L, 1L),
            "AccessDeniedException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }

    @Test
    void testRemoveTreatmentAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                medicalVisitService.removeTreatment(1L, 1L),
            "AuthenticationCredentialsNotFoundException is expected but not thrown");
        Mockito.verify(medicalVisitRepository, Mockito.never()).save(any());
    }
}