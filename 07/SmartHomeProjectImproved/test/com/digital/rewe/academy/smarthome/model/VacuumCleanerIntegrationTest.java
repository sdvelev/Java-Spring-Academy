package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VacuumCleanerIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidator;
    private VacuumCleaner vacuumCleaner;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        vacuumCleaner = new VacuumCleaner("RS106", LocalDate.of(2025, 1, 1),
            BigDecimal.valueOf(60), Placement.MOVABLE, false, BigDecimal.valueOf(100), smartHomeValidator);
    }

    @Test
    void testSetBatteryLevelPositiveCase() {
        BigDecimal newBatteryLevel = BigDecimal.valueOf(10).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        vacuumCleaner.setBatteryLevel(newBatteryLevel);

        assertEquals(newBatteryLevel, vacuumCleaner.getBatteryLevel(),
            "The battery level of the vacuum cleaner is expected to change but remains the old one");
    }

    @Test
    void testSetBatteryLevelWithNegativeValue() {
        assertThrows(NegativeValueException.class, () -> vacuumCleaner.setBatteryLevel(BigDecimal.valueOf(-100)),
            "NegativeValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetMaximumCleaningAreaPositiveCase() {
        BigDecimal newMaximumCleaningArea = BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        VacuumCleaner.setMaximumCleaningArea(newMaximumCleaningArea);

        assertEquals(newMaximumCleaningArea, VacuumCleaner.getMaximumCleaningArea(),
            "The maximum cleaning area of the vacuum cleaner class is expected to change but remains the old one");
    }

    @Test
    void testSetMaximumCleaningAreaWithNegativeValue() {
        assertThrows(NegativeValueException.class, () -> VacuumCleaner.setMaximumCleaningArea(BigDecimal.valueOf(-100)),
            "NegativeValueException expected to be thrown when null value is given");
    }
}