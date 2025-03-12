package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.SmartLightAPI;

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
        this.lightingMode = lightingMode;
    }

    public void setMaximumLumen(BigDecimal maximumLumen) {
        this.maximumLumen = maximumLumen;
    }

    @Override
    public BigDecimal getLumenOutput(LightingMode lightingMode) {
        setLightingMode(lightingMode);
        return switch (lightingMode) {
            case OFF -> BigDecimal.ZERO;
            case STRONG -> this.maximumLumen;
            case MEDIUM -> this.maximumLumen.multiply(BigDecimal.ONE.subtract(MEDIUM_BRIGHTNESS_REDUCTION_PERCENTAGE));
            case DIM -> this.maximumLumen.multiply(BigDecimal.ONE.subtract(DIM_BRIGHTNESS_REDUCTION_PERCENTAGE));
        };
    }
}