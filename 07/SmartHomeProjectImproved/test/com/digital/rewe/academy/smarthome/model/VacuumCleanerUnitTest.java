package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
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

public class VacuumCleanerUnitTest {

    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidatorMock;
    private VacuumCleaner vacuumCleaner;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        vacuumCleaner = new VacuumCleaner("RS106", LocalDate.of(2025, 1, 1),
            BigDecimal.valueOf(60), Placement.MOVABLE, false, BigDecimal.valueOf(100), smartHomeValidatorMock);
    }

    @Test
    void testSetBatteryLevelPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validateBatteryLevel(Mockito.any());
        BigDecimal newBatteryLevel = BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        vacuumCleaner.setBatteryLevel(newBatteryLevel);
        Field field = ReflectionUtils.findFields(VacuumCleaner.class, f -> f.getName().equals("batteryLevel"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualBatteryLevel = (BigDecimal) field.get(vacuumCleaner);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateBatteryLevel(Mockito.any());
        assertEquals(newBatteryLevel, actualBatteryLevel, "The battery level of the vacuum cleaner is expected to change but remains the old one");
    }

    @Test
    void testSetBatteryLevelWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(smartHomeValidatorMock).validateBatteryLevel(Mockito.any());
        BigDecimal newBatteryLevel = BigDecimal.valueOf(-100);
        assertThrows(NegativeValueException.class, () -> vacuumCleaner.setBatteryLevel(newBatteryLevel),
            "NegativeValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(VacuumCleaner.class, f -> f.getName().equals("batteryLevel"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualBatteryLevel = (BigDecimal) field.get(vacuumCleaner);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateBatteryLevel(Mockito.any());
        assertEquals(BigDecimal.valueOf(100), actualBatteryLevel, "The battery level of the vacuum cleaner is not expected to change but it has");
    }
}