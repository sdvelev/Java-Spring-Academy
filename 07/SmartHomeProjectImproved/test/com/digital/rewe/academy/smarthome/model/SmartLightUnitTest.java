package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartLightUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidatorMock;
    private SmartLight smartLight;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        smartLight = new SmartLight("RS106", LocalDate.of(2024, 1, 1),
            BigDecimal.valueOf(10), Placement.BUILT_IN, BigDecimal.valueOf(120), smartHomeValidatorMock);
    }

    @Test
    void testSetLightingModePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        LightingMode newLightingMode = LightingMode.STRONG;
        smartLight.setLightingMode(newLightingMode);
        Field field = ReflectionUtils.findFields(SmartLight.class, f -> f.getName().equals("lightingMode"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LightingMode actualLightingMode = (LightingMode) field.get(smartLight);
        Mockito.verify(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        assertEquals(newLightingMode, actualLightingMode, "The lighting mode of the smart light is expected to change but remains the old one");
    }

    @Test
    void testSetLightingModeWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        LightingMode newLightingMode = null;
        assertThrows(NullValueException.class, () -> smartLight.setLightingMode(newLightingMode),
            "NullValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(SmartLight.class, f -> f.getName().equals("lightingMode"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LightingMode actualLightingMode = (LightingMode) field.get(smartLight);
        Mockito.verify(smartHomeValidatorMock).validateLightingMode(Mockito.any());
        assertEquals(LightingMode.OFF, actualLightingMode, "The lighting mode of the smart light is not expected to change");
    }

    @Test
    void testSetMaximumLumenPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validateMaximumLumen(Mockito.any());
        BigDecimal newMaximumLumen = BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        smartLight.setMaximumLumen(newMaximumLumen);
        Field field = ReflectionUtils.findFields(SmartLight.class, f -> f.getName().equals("maximumLumen"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualMaximumLumen = (BigDecimal) field.get(smartLight);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateMaximumLumen(Mockito.any());
        assertEquals(newMaximumLumen, actualMaximumLumen, "The maximum lumen of the smart light is expected to change but remains the old one");
    }

    @Test
    void testSetMaximumLumenWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(smartHomeValidatorMock).validateMaximumLumen(Mockito.any());
        BigDecimal newMaximumLumen = BigDecimal.valueOf(-60);
        assertThrows(NegativeValueException.class, () -> smartLight.setMaximumLumen(newMaximumLumen),
            "NegativeValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(SmartLight.class, f -> f.getName().equals("maximumLumen"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualMaximumLumen = (BigDecimal) field.get(smartLight);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateMaximumLumen(Mockito.any());
        assertEquals(BigDecimal.valueOf(120), actualMaximumLumen, "The maximum lumen of the smart light is not expected to change but it has");
    }
}