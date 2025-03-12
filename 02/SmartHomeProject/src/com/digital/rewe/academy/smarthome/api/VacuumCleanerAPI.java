package com.digital.rewe.academy.smarthome.api;

import java.math.BigDecimal;

/**
 * The VacuumCleanerAPI is an interface to be implemented by all kinds of vacuum cleaners that provides
 * methods to interact with a vacuum cleaner device.
 * It allows cleaning a specified target area and handles various exceptions related to the cleaning process.
 */
public interface VacuumCleanerAPI {

    /**
     * Cleans the specified target area. The vacuum cleaner will stop cleaning if
     * the battery is exhausted before reaching the target.
     * Exceptions that are handled by the method:
     * NullPointerException - if the target area is null;
     * NegativeValueException - if the target area is negative;
     * MaximumCleaningAreaExceededException - if the target area exceeds the maximum cleaning area.
     *
     * @param targetAreaToBeCleaned the area to be cleaned in square meters
     * @return the cleaned area as a BigDecimal
     */
    public BigDecimal cleanTargetArea(BigDecimal targetAreaToBeCleaned);
}