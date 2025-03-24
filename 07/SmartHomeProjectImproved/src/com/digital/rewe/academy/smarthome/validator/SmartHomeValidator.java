package com.digital.rewe.academy.smarthome.validator;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.BrightnessReductionPercentageMissingException;
import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.exception.MaximumCleaningAreaExceededException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.Device;
import com.digital.rewe.academy.smarthome.model.SmartHome;
import com.digital.rewe.academy.smarthome.model.SmartLight;
import com.digital.rewe.academy.smarthome.model.VacuumCleaner;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SmartHomeValidator {
    public void validateModel(String model) {
        if (model == null) {
            throw new NullValueException("The model argument cannot be null");
        }

        if (model.isBlank()) {
            throw new EmptyValueException("The model argument cannot be empty or blank");
        }
    }

    public void validateProductionDate(LocalDate productionDate) {
        if (productionDate == null) {
            throw new NullValueException("The production date argument cannot be null");
        }

        if (productionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The production date argument cannot be in the future");
        }
    }

    public void validatePowerConsumption(BigDecimal powerConsumption) {
        if (powerConsumption == null) {
            throw new NullValueException("The power consumption argument cannot be null");
        }

        if (powerConsumption.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The power consumption argument cannot be negative");
        }
    }

    public void validatePlacement(Placement placement) {
        if (placement == null) {
            throw new NullValueException("The placement argument cannot be null");
        }
    }

    public void validateLightingMode(LightingMode lightingMode) {
        if (lightingMode == null) {
            throw new NullValueException("The lighting mode argument cannot be null");
        }
    }

    public void validateMaximumLumen(BigDecimal maximumLumen) {
        if (maximumLumen == null) {
            throw new NullValueException("The maximum lumen argument cannot be null");
        }

        if (maximumLumen.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The maximum lumen argument cannot be negative");
        }
    }

    public void validatePercentage(BigDecimal percentage) {
        if (percentage == null) {
            throw new NullValueException("The provided percentage argument cannot be null");
        }

        if (percentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The provided percentage argument cannot be negative");
        }
    }

    public void validateDevice(Device device) {
        if (device == null) {
            throw new NullValueException("The provided device argument cannot be null");
        }
    }

    public void validateSmartLight(SmartLight smartLight) {
        if (smartLight == null) {
            throw new NullValueException("The provided smart light argument cannot be null");
        }
    }

    public void validateVacuumCleaner(VacuumCleaner vacuumCleaner) {
        if (vacuumCleaner == null) {
            throw new NullValueException("The provided vacuum cleaner argument cannot be null");
        }
    }

    public void validateSmartHome(SmartHome smartHome) {
        if (smartHome == null) {
            throw new NullValueException("The provided smart home argument cannot be null");
        }
    }

    public void validateLightingModePresent(SmartLight smartLight, LightingMode lightingMode) {
        validateSmartLight(smartLight);
        validateLightingMode(lightingMode);
        if (!smartLight.getBrightnessReductionPercentageMap().containsKey(lightingMode)) {
            throw new BrightnessReductionPercentageMissingException("There is not a defined percentage for the provided lighting mode");
        }
    }

    public void validateTargetAreaToBeCleaned(BigDecimal targetAreaToBeCleaned, BigDecimal maximumCleaningArea) {
        if (targetAreaToBeCleaned == null) {
            throw new NullValueException("The target area cannot be null");
        }

        if (targetAreaToBeCleaned.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The target area cannot be negative value");
        }

        validateMaximumCleaningArea(maximumCleaningArea);

        if (targetAreaToBeCleaned.compareTo(maximumCleaningArea) > 0) {
            throw new MaximumCleaningAreaExceededException("The target area exceeds the possible maximum cleaning area");
        }
    }

    public static void validateMaximumCleaningArea(BigDecimal maximumCleaningArea) {
        if (maximumCleaningArea == null) {
            throw new NullValueException("The maximum cleaning area cannot be null");
        }

        if (maximumCleaningArea.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The maximum cleaning area cannot be negative");
        }
    }

    public void validateBatteryLevel(BigDecimal batteryLevel) {
        if (batteryLevel == null) {
            throw new NullValueException("The battery level cannot be null");
        }

        if (batteryLevel.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The battery level cannot be negative");
        }
    }

    public void validateOwnerName(String ownerName) {
        if (ownerName == null) {
            throw new NullPointerException("The name of the owner cannot be null");
        }

        if (ownerName.isBlank()) {
            throw new EmptyValueException("The name of the owner cannot be empty or blank");
        }
    }
}