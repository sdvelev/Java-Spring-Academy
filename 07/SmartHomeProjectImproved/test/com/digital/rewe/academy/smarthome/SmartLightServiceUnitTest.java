package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.SmartLight;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartLightServiceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private SmartHomeValidator smartHomeValidatorMock;
    private SmartLightService smartLightService;
    @Mock
    private SmartLight smartLightMock;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        smartLightService = new SmartLightService(smartHomeValidatorMock);
        smartLightMock = Mockito.mock(SmartLight.class);
    }

    @Test
    void testAddBrightnessReductionPercentagePositiveCase() {

        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartLight(Mockito.any());
        Mockito.doNothing().when(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        Mockito.doNothing().when(smartHomeValidatorMock).validatePercentage(Mockito.any());

        Map<LightingMode, BigDecimal> brightnessReductionPercentageMap = new EnumMap<>(LightingMode.class);
        Mockito.when(smartLightMock.getBrightnessReductionPercentageMap()).thenReturn(brightnessReductionPercentageMap);

        smartLightService.addBrightnessReductionPercentage(smartLightMock, LightingMode.STRONG, BigDecimal.ZERO);

        assertFalse(brightnessReductionPercentageMap.isEmpty(),
            "The collection with brightness reduction percentages cannot be empty after addition");

        assertIterableEquals(brightnessReductionPercentageMap.entrySet(),
            Map.of(LightingMode.STRONG, BigDecimal.ZERO).entrySet(),
            "The elements in the collection are not the same as the expected");
    }

    @Test
    void testAddBrightnessReductionPercentageWithNullLightingMode() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartLight(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        assertThrows(NullValueException.class, () -> smartLightService.addBrightnessReductionPercentage(smartLightMock,
            null, BigDecimal.ZERO), "NullValueException is expected but not thrown");
    }

    @Test
    void testAddBrightnessReductionPercentageWithNullPercentage() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartLight(Mockito.any());
        Mockito.doNothing().when(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validatePercentage(Mockito.any());
        assertThrows(NullValueException.class, () -> smartLightService.addBrightnessReductionPercentage(smartLightMock,
            LightingMode.STRONG, null), "NullValueException is expected but not thrown");
    }

    @Test
    void testAddBrightnessReductionPercentageWithNegativePercentage() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartLight(Mockito.any());
        Mockito.doNothing().when(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        Mockito.doThrow(NegativeValueException.class).when(smartHomeValidatorMock).validatePercentage(Mockito.any());
        assertThrows(NegativeValueException.class, () -> smartLightService.addBrightnessReductionPercentage(smartLightMock,
            LightingMode.STRONG, BigDecimal.valueOf(-0.6)), "NegativeValueException is expected but not thrown");
    }

    @Test
    void testGetLumenOutputPositiveCase() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateLightingModePresent(Mockito.any(), Mockito.any());

        Mockito.when(smartLightMock.getMaximumLumen()).thenReturn(BigDecimal.valueOf(100));

        Mockito.doNothing().when(smartLightMock).setLightingMode(Mockito.any());

        Map<LightingMode, BigDecimal> brightnessReductionPercentageMap = new EnumMap<>(LightingMode.class);
        brightnessReductionPercentageMap.put(LightingMode.DIM, BigDecimal.valueOf(0.4));
        Mockito.when(smartLightMock.getBrightnessReductionPercentageMap()).thenReturn(brightnessReductionPercentageMap);

        smartLightService.addBrightnessReductionPercentage(smartLightMock, LightingMode.STRONG, BigDecimal.ZERO);

        assertEquals(BigDecimal.valueOf(60).setScale(ROUNDING_SCALE_FACTOR,RoundingMode.HALF_UP),
            smartLightService.getLumenOutput(smartLightMock, LightingMode.DIM),
            "The actual lumen output is not the same as the expected");
    }

    @Test
    void testGetLumenOutputWithNullLightingMode() {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateLightingModePresent(Mockito.any(), Mockito.any());
        assertThrows(NullValueException.class, () -> smartLightService.getLumenOutput(smartLightMock, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testGetLumenOutputWithNullSmartLight() {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateLightingModePresent(Mockito.any(), Mockito.any());
        assertThrows(NullValueException.class, () -> smartLightService.getLumenOutput(null, LightingMode.DIM),
            "NullValueException is expected but not thrown");
    }
}