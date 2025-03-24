package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.SmartLightServiceAPI;
import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.model.SmartLight;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmartLightService implements SmartLightServiceAPI {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidator;

    public SmartLightService(SmartHomeValidator smartHomeValidator) {
        this.smartHomeValidator = smartHomeValidator;
    }

    @Override
    public void addBrightnessReductionPercentage(SmartLight smartLight, LightingMode lightingMode, BigDecimal percentage) {
        smartHomeValidator.validateSmartLight(smartLight);
        smartHomeValidator.validateLightingMode(lightingMode);
        smartHomeValidator.validatePercentage(percentage);
        smartLight.getBrightnessReductionPercentageMap().put(lightingMode, percentage);
    }

    @Override
    public BigDecimal getLumenOutput(SmartLight smartLight, LightingMode lightingMode) {
        smartHomeValidator.validateLightingModePresent(smartLight, lightingMode);
        smartLight.setLightingMode(lightingMode);

        return lightingMode.equals(LightingMode.OFF) ? BigDecimal.ZERO
            : smartLight.getMaximumLumen().multiply(BigDecimal.ONE.subtract(smartLight
            .getBrightnessReductionPercentageMap().get(lightingMode)))
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }
}