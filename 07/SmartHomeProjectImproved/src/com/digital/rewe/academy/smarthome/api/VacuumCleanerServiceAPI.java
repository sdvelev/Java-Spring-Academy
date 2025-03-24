package com.digital.rewe.academy.smarthome.api;

import com.digital.rewe.academy.smarthome.exception.MaximumCleaningAreaExceededException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.VacuumCleaner;

import java.math.BigDecimal;

public interface VacuumCleanerServiceAPI {

    /**
     * Cleans the target area specified for the vacuum cleaner.
     *
     * @param vacuumCleaner the vacuum cleaner to be used
     * @param targetAreaToBeCleaned the area to be cleaned
     * @return the cleaned area as a BigDecimal
     * @throws NullValueException if the vacuum cleaner or target area is null
     * @throws NegativeValueException if the target area is negative
     * @throws MaximumCleaningAreaExceededException if the target area exceeds the possible maximum cleaning area
     */
     BigDecimal cleanTargetArea(VacuumCleaner vacuumCleaner, BigDecimal targetAreaToBeCleaned);

    /**
     * Calculates the battery consumption rate for the vacuum cleaner.
     *
     * @param vacuumCleaner the vacuum cleaner for which the battery consumption rate is to be calculated
     * @return the battery consumption rate as a BigDecimal
     * @throws NullValueException if the vacuum cleaner is null
     */
     BigDecimal calculateBatteryConsumptionRate(VacuumCleaner vacuumCleaner);
}