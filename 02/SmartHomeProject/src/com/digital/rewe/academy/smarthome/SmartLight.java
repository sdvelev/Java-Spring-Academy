package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.SmartLightAPI;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SmartLight extends Device implements SmartLightAPI {
    private static final String IDENTIFIER = "SMARTLIGHT-";
    private static final BigDecimal MEDIUM_BRIGHTNESS_REDUCTION_PERCENTAGE = new BigDecimal("0.25");
    private static final BigDecimal DIM_BRIGHTNESS_REDUCTION_PERCENTAGE = new BigDecimal("0.5");

    private static long counterOfInstances = 0;

    private LightingMode lightingMode;
    private BigDecimal maximumLumen;

    public SmartLight(String model, LocalDate productionDate, BigDecimal powerConsumption,
                      Placement placement, BigDecimal maximumLumen) {
        super(model, productionDate, powerConsumption, placement);
        validateMaximumLumen(maximumLumen);
        this.lightingMode = LightingMode.OFF;
        this.maximumLumen = maximumLumen;
        setId(IDENTIFIER + ++counterOfInstances);
    }

    public LightingMode getLightingMode() {
        return lightingMode;
    }

    public BigDecimal getMaximumLumen() {
        return maximumLumen;
    }

    public void setLightingMode(LightingMode lightingMode) {
        validateLightingMode(lightingMode);
        this.lightingMode = lightingMode;
    }

    public void setMaximumLumen(BigDecimal maximumLumen) {
        validateMaximumLumen(maximumLumen);
        this.maximumLumen = maximumLumen;
    }

    @Override
    public BigDecimal getLumenOutput(LightingMode lightingMode) {
        try {
            setLightingMode(lightingMode);
        } catch (NullValueException e) {
            System.out.println(e.getMessage());
            return BigDecimal.ZERO;
        }

        return switch (lightingMode) {
            case OFF -> BigDecimal.ZERO;
            case STRONG -> this.maximumLumen;
            case MEDIUM -> this.maximumLumen.multiply(BigDecimal.ONE.subtract(MEDIUM_BRIGHTNESS_REDUCTION_PERCENTAGE));
            case DIM -> this.maximumLumen.multiply(BigDecimal.ONE.subtract(DIM_BRIGHTNESS_REDUCTION_PERCENTAGE));
        };
    }

    private void validateLightingMode(LightingMode lightingMode) {
        if (lightingMode == null) {
            throw new NullValueException("The lighting mode argument cannot be null");
        }
    }

    private void validateMaximumLumen(BigDecimal maximumLumen) {
        if (maximumLumen == null) {
            throw new NullValueException("The maximum lumen argument cannot be null");
        }

        if (maximumLumen.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The maximum lumen argument cannot be negative");
        }
    }
}