package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.MaximumCleaningAreaExceededException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.VacuumCleaner;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VacuumCleanerServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidator;

    private VacuumCleanerService vacuumCleanerService;
    private VacuumCleaner vacuumCleaner;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        vacuumCleanerService = new VacuumCleanerService(smartHomeValidator);
        vacuumCleaner = new VacuumCleaner("RS106", LocalDate.of(2025, 1, 1),
            BigDecimal.valueOf(60), Placement.MOVABLE, false, BigDecimal.valueOf(100), smartHomeValidator);
    }

    @Test
    void testCleanTargetAreaPositiveCase() {
        BigDecimal cleanedArea0 = vacuumCleanerService.cleanTargetArea(vacuumCleaner, BigDecimal.valueOf(20));
        assertEquals(BigDecimal.valueOf(20).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), cleanedArea0,
            "All target area has to be cleaned but it is not actually");

        BigDecimal cleanedArea = vacuumCleanerService.cleanTargetArea(vacuumCleaner, BigDecimal.valueOf(70));
        assertEquals(BigDecimal.valueOf(70).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), cleanedArea,
                "All target area has to be cleaned but it is not actually");

        BigDecimal cleanedArea2 = vacuumCleanerService.cleanTargetArea(vacuumCleaner, BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(2).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), cleanedArea2,
            "All target area has to be cleaned but it is not actually");

        BigDecimal cleanedArea3 = vacuumCleanerService.cleanTargetArea(vacuumCleaner, BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(2).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), cleanedArea3,
            "All target area has to be cleaned but it is not actually");

        BigDecimal cleanedArea4 = vacuumCleanerService.cleanTargetArea(vacuumCleaner, BigDecimal.valueOf(99));
        assertEquals(BigDecimal.ZERO.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), cleanedArea4,
            "No area is expected to be cleaned after battery drained but it is actually");
    }

    @Test
    void testCleanTargetAreaMaximumCleaningAreaExceeded() {
        assertThrows(MaximumCleaningAreaExceededException.class, () -> vacuumCleanerService.cleanTargetArea(vacuumCleaner,
                BigDecimal.valueOf(2000)), "MaximumCleaningAreaExceededException is expected but not thrown");
    }

    @Test
    void testCalculateBatteryConsumptionRatePositiveCase() {
        assertEquals(BigDecimal.TEN.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            vacuumCleanerService.calculateBatteryConsumptionRate(vacuumCleaner).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            "The returned battery consumption rate is not the same as the expected");
    }

    @Test
    void testCalculateBatteryConsumptionRateWithNullVacuumCleaner() {
        assertThrows(NullValueException.class,
            () -> vacuumCleanerService.calculateBatteryConsumptionRate(null),
            "NullValueException is expected but not thrown");
    }
}