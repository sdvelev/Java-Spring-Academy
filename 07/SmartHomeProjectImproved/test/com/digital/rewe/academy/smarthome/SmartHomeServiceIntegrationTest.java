package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.SmartHome;
import com.digital.rewe.academy.smarthome.model.SmartLight;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartHomeServiceIntegrationTest {

    private SmartHomeValidator smartHomeValidator;

    private SmartHomeService smartHomeService;
    private SmartHome smartHome;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        smartHomeService = new SmartHomeService(smartHomeValidator);
        smartHome = new SmartHome(1, "Stefan", smartHomeValidator);
    }

    @Test
    void testAddDevicePositiveCase() {
        SmartLight smartLight = new SmartLight("RS106", LocalDate.of(2024, 1, 1),
            BigDecimal.valueOf(10), Placement.BUILT_IN, BigDecimal.valueOf(120), smartHomeValidator);

        smartHomeService.addDevice(smartHome, smartLight);

        assertTrue(smartHome.getDevices().contains(smartLight),
            "The list of devices is not expected to be empty but it is");
    }

    @Test
    void testAddDeviceWithNullDevice() {
        assertThrows(NullValueException.class, () -> smartHomeService.addDevice(smartHome, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testRemoveDevicePositiveCase() {
        SmartLight smartLight = new SmartLight("RS106", LocalDate.of(2024, 1, 1),
            BigDecimal.valueOf(10), Placement.BUILT_IN, BigDecimal.valueOf(120), smartHomeValidator);

        smartHomeService.addDevice(smartHome, smartLight);

        assertTrue(smartHome.getDevices().contains(smartLight),
            "The list of devices is not expected to be empty but it is");

        smartHomeService.removeDevice(smartHome, smartLight);
        assertTrue(smartHome.getDevices().isEmpty(),
            "The list of devices is expected to be empty after removal but it is not");
    }

    @Test
    void testRemoveDeviceWithNullDevice() {
        assertThrows(NullValueException.class, () -> smartHomeService.removeDevice(smartHome, null),
            "NullValueException is expected but not thrown");
    }
}