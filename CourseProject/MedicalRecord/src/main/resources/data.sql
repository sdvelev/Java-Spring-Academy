-- Insert dummy data for Role
INSERT INTO role (id, authority) VALUES
(1, 'PATIENT'),
(2, 'DOCTOR'),
(3, 'ADMIN');

-- Insert dummy data for User
INSERT INTO user (id, username, password) VALUES
(1, 'john.doe', '$2a$12$ub2Ywu949Wg22BmObfUfTugosAPDmT7iFYvQsD1VCMBRIxsjYws4a'),
(2, 'jane.smith', '$2a$12$UJ4Hfr8XLdLxzKJtC8tfoeyNmOPdResD6pzip48nGkWrXg9GFFch.'),
(3, 'alice.jones', '$2a$12$aI9ym4OjL6K/FGUrGDyTkOX1m0ipmq7D1Uf3bYGee..AxLK6mm1Kq'),
(4, 'bob.brown', '$2a$12$Hy2SlG7fuuvctlOEbgkIKOPdCW1QcSUevMq5HNz6iypOW5sh5A6G6'),
(5, 'admin', '$2a$12$djR8aMCP5L9.DH/ZN7kZ1uiRYBSbxooxN5ZLVVBtwAZ7/gvUK8W7u');

-- Insert dummy data for UserHasRole                                                                                                                                            (4, '1995-04-04', 'bob.brown@example.com', 'Bob', 'Brown', 'D', 'password', '2233445566', '555-6789', 'bobbrown', 2);
INSERT INTO user_has_role(user_id, role_id) VALUES
(1, 2),
(2, 2),
(3, 1),
(4, 1),
(5, 3);

-- Insert dummy data for Address
INSERT INTO address (id, apartment_number, country_name, district, dwelling_number, entrance, floor_number, municipality, populated_place, postal_code, region, street_name) VALUES
(1, '101', 'USA', 'District 1', '10', 'A', '1', 'Municipality 1', 'Springfield', '62701', 'Region 1', 'Main St'),
(2, '202', 'USA', 'District 2', '20', 'B', '2', 'Municipality 2', 'Springfield', '62702', 'Region 2', 'Elm St'),
(3, '303', 'USA', 'District 3', '30', 'C', '3', 'Municipality 3', 'Springfield', '62703', 'Region 3', 'Oak St'),
(4, '404', 'USA', 'District 4', '40', 'D', '4', 'Municipality 4', 'Springfield', '62704', 'Region 4', 'Pine St');

-- Insert dummy data for Person
INSERT INTO person (id, birthdate, email, first_name, last_name, middle_name, personal_id_number, phone_number, address_id) VALUES
(1, '1980-01-01', 'john.doe@example.com', 'John', 'Doe', 'A', '1234567890', '555-1234', 1),
(2, '1990-02-02', 'jane.smith@example.com', 'Jane', 'Smith', 'B', '0987654321', '555-5678', 2),
(3, '1985-03-03', 'alice.jones@example.com', 'Alice', 'Jones', 'C', '1122334455', '555-7890', 1),
(4, '1995-04-04', 'bob.brown@example.com', 'Bob', 'Brown', 'D', '2233445566', '555-6789', 2);

-- Insert dummy data for Specialty
INSERT INTO specialty (id, description, specialty_name) VALUES
(1, 'Heart specialist', 'Cardiology'),
(2, 'Brain specialist', 'Neurology');

-- Insert dummy data for Doctor
INSERT INTO doctor (unique_id_number, id) VALUES
('D123456', 1),
('D654321', 2);

-- Insert dummy data for GeneralPractitioner
INSERT INTO general_practitioner (practice_number, id, practice_address_id) VALUES
('GP123456', 1, 3),
('GP654321', 2, 4);

-- Insert dummy data for Patient
INSERT INTO patient (last_paid_health_insurance, id, general_practitioner_id) VALUES
('2025-03-01', 3, 2),
('2025-03-01', 4, 1);

-- Insert dummy data for MedicalVisit
INSERT INTO medical_visit (id, date_of_visit, doctor_id, patient_id) VALUES
(1, '2025-03-30', 1, 3),
(2, '2025-03-29', 2, 4);

-- Insert dummy data for Diagnose
INSERT INTO diagnose (id, diagnose_description, icd_code) VALUES
(1, 'Common Cold', 'J00'),
(2, 'Flu', 'J10');

-- Insert dummy data for MedicalVisitHasDiagnose
INSERT INTO medical_visit_has_diagnose (medical_visit_id, diagnose_id) VALUES
(1, 1),
(2, 2);

-- Insert dummy data for Treatment
INSERT INTO treatment (id, description, name) VALUES
(1, 'Rest and hydration', 'Rest'),
(2, 'Antiviral medication', 'Antiviral');

-- Insert dummy data for MedicalVisitHasTreatment
INSERT INTO medical_visit_has_treatment (medical_visit_id, treatment_id) VALUES
(1, 1),
(2, 2);

-- Insert dummy data for SickLeave
INSERT INTO sick_leave (id, description, end_date, start_date, medical_visit_id) VALUES
(1, 'Sick leave for flu', '2025-04-06', '2025-03-30', 1),
(2, 'Sick leave for cold', '2025-03-31', '2025-03-29', 2);

-- Insert dummy data for DoctorHasSpecialty
INSERT INTO doctor_has_specialty (doctor_id, specialty_id) VALUES
(1, 1),
(2, 2);