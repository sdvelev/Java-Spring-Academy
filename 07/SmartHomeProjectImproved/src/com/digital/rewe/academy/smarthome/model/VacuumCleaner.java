package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class VacuumCleaner extends Device {
    private static final String IDENTIFIER = "VACUUMCLEANER-";
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private static BigDecimal batteryConsumptionPercentageWithoutMop = new BigDecimal("0.1");;
    private static BigDecimal maximumCleaningArea = new BigDecimal("100");

    private static long counterOfInstances = 0;

    private boolean isMop;
    private BigDecimal batteryLevel;

    public VacuumCleaner(String model, LocalDate productionDate, BigDecimal powerConsumption,
                         Placement placement, boolean isMop, BigDecimal batteryLevel, SmartHomeValidator smartHomeValidator) {
        super(model, productionDate, powerConsumption, placement, smartHomeValidator);
        super.setId(IDENTIFIER + ++counterOfInstances);
        this.setBatteryLevel(batteryLevel);
        this.setMop(isMop);
    }

    public boolean isMop() {
        return isMop;
    }

    public void setMop(boolean mop) {
        isMop = mop;
    }

    public BigDecimal getBatteryLevel() {
        return batteryLevel.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public BigDecimal getPreciseBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(BigDecimal batteryLevel) {
        super.getSmartHomeValidator().validateBatteryLevel(batteryLevel);
        this.batteryLevel = batteryLevel;
    }

    public static BigDecimal getBatteryConsumptionPercentageWithoutMop() {
        return batteryConsumptionPercentageWithoutMop;
    }

    public static BigDecimal getMaximumCleaningArea() {
        return maximumCleaningArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public static void setMaximumCleaningArea(BigDecimal maximumCleaningArea) {
        SmartHomeValidator.validateMaximumCleaningArea(maximumCleaningArea);
        VacuumCleaner.maximumCleaningArea = maximumCleaningArea;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VacuumCleaner that = (VacuumCleaner) o;
        return isMop == that.isMop && Objects.equals(batteryLevel, that.batteryLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isMop, batteryLevel);
    }
}