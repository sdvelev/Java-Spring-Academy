package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.SmartLight;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartLightServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidator;

    private SmartLightService smartLightService;
    private SmartLight smartLight;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        smartLightService = new SmartLightService(smartHomeValidator);
        smartLight = new SmartLight("RS106", LocalDate.of(2024, 1, 1),
            BigDecimal.valueOf(10), Placement.BUILT_IN, BigDecimal.valueOf(120), smartHomeValidator);
    }

    @Test
    void testAddBrightnessReductionPercentagePositiveCase() {
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.STRONG, BigDecimal.ZERO);
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.MEDIUM, BigDecimal.valueOf(0.25));
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.DIM, BigDecimal.valueOf(0.5));

        Map<LightingMode, BigDecimal> actualResult = smartLight.getBrightnessReductionPercentageMap();
        assertTrue(actualResult.containsKey(LightingMode.STRONG) && actualResult.get(LightingMode.STRONG).equals(BigDecimal.ZERO),
            "The actual result after adding brightness percentage reduction is not the same as the expected");
        assertTrue(actualResult.containsKey(LightingMode.MEDIUM) && actualResult.get(LightingMode.MEDIUM).equals(BigDecimal.valueOf(0.25)),
            "The actual result after adding brightness percentage reduction is not the same as the expected");
        assertTrue(actualResult.containsKey(LightingMode.DIM) && actualResult.get(LightingMode.DIM).equals(BigDecimal.valueOf(0.5)),
            "The actual result after adding brightness percentage reduction is not the same as the expected");
    }

    @Test
    void testAddBrightnessReductionPercentageWithNullLightingMode() {
        assertThrows(NullValueException.class, () -> smartLightService.addBrightnessReductionPercentage(smartLight,
            null, BigDecimal.ZERO), "NullValueException is expected but not thrown");
    }

    @Test
    void testAddBrightnessReductionPercentageWithNullPercentage() {
        assertThrows(NullValueException.class, () -> smartLightService.addBrightnessReductionPercentage(smartLight,
            LightingMode.STRONG, null), "NullValueException is expected but not thrown");
    }

    @Test
    void testAddBrightnessReductionPercentageWithNegativePercentage() {
        assertThrows(NegativeValueException.class, () -> smartLightService.addBrightnessReductionPercentage(smartLight,
            LightingMode.STRONG, BigDecimal.valueOf(-0.6)), "NegativeValueException is expected but not thrown");
    }

    @Test
    void testGetLumenOutputPositiveCase() {
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.STRONG, BigDecimal.ZERO);
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.MEDIUM, BigDecimal.valueOf(0.25));
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.DIM, BigDecimal.valueOf(0.5));

        assertEquals(BigDecimal.valueOf(120).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            smartLightService.getLumenOutput(smartLight, LightingMode.STRONG),
            "The expected lumen output in strong lighting mode is not the same as the expected");

        assertEquals(BigDecimal.valueOf(90).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            smartLightService.getLumenOutput(smartLight, LightingMode.MEDIUM),
            "The expected lumen output in medium lighting mode is not the same as the expected");

        assertEquals(BigDecimal.valueOf(60).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            smartLightService.getLumenOutput(smartLight, LightingMode.DIM),
            "The expected lumen output in dim lighting mode is not the same as the expected");
    }

    @Test
    void testGetLumenOutputWithNullLightingMode() {
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.DIM, BigDecimal.valueOf(0.5));
        assertThrows(NullValueException.class, () -> smartLightService.getLumenOutput(smartLight, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testGetLumenOutputWithNullSmartLight() {
        smartLightService.addBrightnessReductionPercentage(smartLight, LightingMode.DIM, BigDecimal.valueOf(0.5));
        assertThrows(NullValueException.class, () -> smartLightService.getLumenOutput(null, LightingMode.DIM),
            "NullValueException is expected but not thrown");
    }
}