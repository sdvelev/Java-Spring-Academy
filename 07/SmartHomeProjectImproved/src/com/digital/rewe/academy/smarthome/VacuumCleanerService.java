package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.VacuumCleanerServiceAPI;
import com.digital.rewe.academy.smarthome.model.VacuumCleaner;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class VacuumCleanerService implements VacuumCleanerServiceAPI {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidator;

    public VacuumCleanerService(SmartHomeValidator smartHomeValidator) {
        this.smartHomeValidator = smartHomeValidator;
    }

    @Override
    public BigDecimal cleanTargetArea(VacuumCleaner vacuumCleaner, BigDecimal targetAreaToBeCleaned) {
        smartHomeValidator.validateVacuumCleaner(vacuumCleaner);
        smartHomeValidator.validateTargetAreaToBeCleaned(targetAreaToBeCleaned, VacuumCleaner.getMaximumCleaningArea());
        BigDecimal cleanedArea = BigDecimal.ZERO;
        while (targetAreaToBeCleaned.compareTo(BigDecimal.ZERO) > 0) {
            if (targetAreaToBeCleaned.compareTo(BigDecimal.ONE) > 0) {
                BigDecimal batteryConsumptionRate = calculateBatteryConsumptionRate(vacuumCleaner);
                if (compareBatteryConsumptionWithTotalBattery(vacuumCleaner, batteryConsumptionRate)) {
                    decreaseBatteryLevel(vacuumCleaner, batteryConsumptionRate);
                    return cleanedArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
                }
                decreaseBatteryLevel(vacuumCleaner, batteryConsumptionRate);
                cleanedArea = cleanedArea.add(BigDecimal.ONE);
                targetAreaToBeCleaned = targetAreaToBeCleaned.subtract(BigDecimal.ONE);
            } else {
                BigDecimal batteryConsumptionRate = calculateBatteryConsumptionRate(vacuumCleaner).multiply(targetAreaToBeCleaned);
                if (compareBatteryConsumptionWithTotalBattery(vacuumCleaner, batteryConsumptionRate)) {
                    decreaseBatteryLevel(vacuumCleaner, batteryConsumptionRate);
                    return cleanedArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
                }
                decreaseBatteryLevel(vacuumCleaner, batteryConsumptionRate);
                cleanedArea = cleanedArea.add(targetAreaToBeCleaned);
                targetAreaToBeCleaned = BigDecimal.ZERO;
            }
        }
        return cleanedArea.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateBatteryConsumptionRate(VacuumCleaner vacuumCleaner) {
        smartHomeValidator.validateVacuumCleaner(vacuumCleaner);
        return vacuumCleaner.isMop()
            ? vacuumCleaner.getPreciseBatteryLevel().multiply(VacuumCleaner.getBatteryConsumptionPercentageWithoutMop()).multiply(BigDecimal.TWO)
            : vacuumCleaner.getPreciseBatteryLevel().multiply(VacuumCleaner.getBatteryConsumptionPercentageWithoutMop());
    }

    private boolean compareBatteryConsumptionWithTotalBattery(VacuumCleaner vacuumCleaner, BigDecimal batteryConsumptionRate) {
        return batteryConsumptionRate.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)
            .compareTo(vacuumCleaner.getBatteryLevel()) >= 0;
    }

    private void decreaseBatteryLevel(VacuumCleaner vacuumCleaner, BigDecimal batteryConsumptionRate) {
        vacuumCleaner.setBatteryLevel(vacuumCleaner.getPreciseBatteryLevel().subtract(batteryConsumptionRate));
    }
}