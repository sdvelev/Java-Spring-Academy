package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.exception.MaximumCleaningAreaExceededException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.VacuumCleaner;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VacuumCleanerServiceUnitTest {


    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private SmartHomeValidator smartHomeValidatorMock;
    private VacuumCleanerService vacuumCleanerService;
    @Mock
    private VacuumCleaner vacuumCleanerMock;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        vacuumCleanerService = new VacuumCleanerService(smartHomeValidatorMock);
        vacuumCleanerMock = Mockito.mock(VacuumCleaner.class);
    }


    @Test
    void testCleanTargetAreaMaximumCleaningAreaExceeded() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateVacuumCleaner(Mockito.any());
        Mockito.doThrow(MaximumCleaningAreaExceededException.class).when(smartHomeValidatorMock).validateTargetAreaToBeCleaned(Mockito.any(), Mockito.any());
        assertThrows(MaximumCleaningAreaExceededException.class, () -> vacuumCleanerService.cleanTargetArea(vacuumCleanerMock,
            BigDecimal.valueOf(2000)), "MaximumCleaningAreaExceededException is expected but not thrown");
    }

    @Test
    void testCalculateBatteryConsumptionRateWithMopPositiveCase() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateVacuumCleaner(Mockito.any());
        Mockito.when(vacuumCleanerMock.isMop()).thenReturn(true);
        Mockito.when(vacuumCleanerMock.getPreciseBatteryLevel()).thenReturn(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(20).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            vacuumCleanerService.calculateBatteryConsumptionRate(vacuumCleanerMock).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            "The returned battery consumption rate is not the same as the expected");
    }

    @Test
    void testCalculateBatteryConsumptionRateWithNullVacuumCleaner() {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateVacuumCleaner(Mockito.any());
        assertThrows(NullValueException.class,
            () -> vacuumCleanerService.calculateBatteryConsumptionRate(null),
            "NullValueException is expected but not thrown");
    }
}