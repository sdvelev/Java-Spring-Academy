package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.VacuumCleanerAPI;
import com.digital.rewe.academy.smarthome.exception.MaximumCleaningAreaExceededException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class VacuumCleaner extends Device implements VacuumCleanerAPI {
    private static final String IDENTIFIER = "VACUUMCLEANER-";
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private static BigDecimal batteryConsumptionPercentageWithoutMop;
    private static BigDecimal batteryConsumptionPercentageWithMop;
    private static BigDecimal maximumCleaningArea;

    private static long counterOfInstances = 0;

     static {
        maximumCleaningArea = new BigDecimal("100");
        batteryConsumptionPercentageWithoutMop = new BigDecimal("0.1");
        batteryConsumptionPercentageWithMop =
            batteryConsumptionPercentageWithoutMop.multiply(BigDecimal.TWO).compareTo(BigDecimal.ONE) <= 0
            ? batteryConsumptionPercentageWithoutMop.multiply(BigDecimal.TWO)
            : BigDecimal.ONE;
    }

    private boolean isMop;
    private BigDecimal batteryLevel;

    public VacuumCleaner(String model, LocalDate productionDate, BigDecimal powerConsumption,
                      Placement placement, boolean isMop, BigDecimal batteryLevel) {
        super(model, productionDate, powerConsumption, placement);
        this.batteryLevel = batteryLevel;
        this.isMop = isMop;
        setId(IDENTIFIER + ++counterOfInstances);
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

    public void setBatteryLevel(BigDecimal batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public static BigDecimal getMaximumCleaningArea() {
        return maximumCleaningArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public static void setMaximumCleaningArea(BigDecimal maximumCleaningArea) {
        VacuumCleaner.maximumCleaningArea = maximumCleaningArea;
    }

    @Override
    public BigDecimal cleanTargetArea(BigDecimal targetAreaToBeCleaned) {
        try {
            validateTargetAreaToBeCleaned(targetAreaToBeCleaned);
            BigDecimal cleanedArea = BigDecimal.ZERO;
            while (targetAreaToBeCleaned.compareTo(BigDecimal.ZERO) > 0) {
                if (targetAreaToBeCleaned.compareTo(BigDecimal.ONE) > 0) {
                    BigDecimal batteryConsumptionRate = calculateBatteryConsumptionRate();
                    if (compareBatteryConsumptionWithTotalBattery(batteryConsumptionRate)) {
                        decreaseBatteryLevel(batteryConsumptionRate);
                        return cleanedArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
                    }
                    decreaseBatteryLevel(batteryConsumptionRate);
                    cleanedArea = cleanedArea.add(BigDecimal.ONE);
                    targetAreaToBeCleaned = targetAreaToBeCleaned.subtract(BigDecimal.ONE);
                } else {
                    BigDecimal batteryConsumptionRate = calculateBatteryConsumptionRate().multiply(targetAreaToBeCleaned);
                    if (compareBatteryConsumptionWithTotalBattery(batteryConsumptionRate)) {
                        decreaseBatteryLevel(batteryConsumptionRate);
                        return cleanedArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
                    }
                    decreaseBatteryLevel(batteryConsumptionRate);
                    cleanedArea = cleanedArea.add(targetAreaToBeCleaned);
                    targetAreaToBeCleaned = BigDecimal.ZERO;
                }
            }
            return cleanedArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        } catch (NullPointerException | NegativeValueException | MaximumCleaningAreaExceededException e) {
            System.out.println(e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateBatteryConsumptionRate() {
        return isMop
            ? getBatteryLevel().multiply(batteryConsumptionPercentageWithoutMop).multiply(BigDecimal.TWO)
            : getBatteryLevel().multiply(batteryConsumptionPercentageWithoutMop);
    }

    private boolean compareBatteryConsumptionWithTotalBattery(BigDecimal batteryConsumptionRate) {
        return batteryConsumptionRate.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)
            .compareTo(getBatteryLevel().setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)) >= 0;
    }

    private void decreaseBatteryLevel(BigDecimal batteryConsumptionRate) {
        setBatteryLevel(batteryLevel.subtract(batteryConsumptionRate));
    }

    private void validateTargetAreaToBeCleaned(BigDecimal targetAreaToBeCleaned)
        throws MaximumCleaningAreaExceededException, NegativeValueException {
        validateTargetAreToBeCleanedForNullPointer(targetAreaToBeCleaned);
        validateTargetAreaToBeCleanedForExcess(targetAreaToBeCleaned);
        validateTargetAreaToBeCleanedForNegativeValue(targetAreaToBeCleaned);
    }

    private void validateTargetAreToBeCleanedForNullPointer(BigDecimal targetAreaToBeCleaned) {
        if (targetAreaToBeCleaned == null) {
            throw new NullPointerException("The target area cannot be null");
        }
    }

    private void validateTargetAreaToBeCleanedForExcess(BigDecimal targetAreaToBeCleaned) throws MaximumCleaningAreaExceededException {
        if (targetAreaToBeCleaned.compareTo(maximumCleaningArea) > 0) {
            throw new MaximumCleaningAreaExceededException("The target area exceeds the possible maximum cleaning area");
        }
    }

    private void validateTargetAreaToBeCleanedForNegativeValue(BigDecimal targetAreaToBeCleaned) throws NegativeValueException {
        if (targetAreaToBeCleaned.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The target area cannot be negative value");
        }
    }
}