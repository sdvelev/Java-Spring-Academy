package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartLightIntegrationTest {

    private SmartHomeValidator smartHomeValidator;
    private SmartLight smartLight;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        smartLight = new SmartLight("RS106", LocalDate.of(2024, 1, 1),
            BigDecimal.valueOf(10), Placement.BUILT_IN, BigDecimal.valueOf(120), smartHomeValidator);
    }

    @Test
    void testSetLightingModePositiveCase() {
        LightingMode newLightingMode = LightingMode.STRONG;
        smartLight.setLightingMode(newLightingMode);

        assertEquals(newLightingMode, smartLight.getLightingMode(),
            "The lighting mode of the smart light is expected to change but remains the old one");
    }

    @Test
    void testSetLightingModeWithNullValue() {
        assertThrows(NullValueException.class, () -> smartLight.setLightingMode(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetMaximumLumenPositiveCase() {
        BigDecimal newMaximumLumen = BigDecimal.valueOf(100);
        smartLight.setMaximumLumen(newMaximumLumen);

        assertEquals(newMaximumLumen, smartLight.getMaximumLumen(),
            "The maximum lumen of the smart light is expected to change but remains the old one");
    }

    @Test
    void testSetMaximumLumenWithNegativeValue() {
        assertThrows(NegativeValueException.class, () -> smartLight.setMaximumLumen(BigDecimal.valueOf(-60)),
            "NegativeValueException expected to be thrown when null value is given");
    }
}