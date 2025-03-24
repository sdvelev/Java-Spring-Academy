package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class SmartLight extends Device {
    private static final String IDENTIFIER = "SMARTLIGHT-";

    private static long counterOfInstances = 0;

    private LightingMode lightingMode;
    private BigDecimal maximumLumen;
    private final Map<LightingMode, BigDecimal> brightnessReductionPercentageMap;

    public SmartLight(String model, LocalDate productionDate, BigDecimal powerConsumption,
                      Placement placement, BigDecimal maximumLumen, SmartHomeValidator smartHomeValidator) {
        super(model, productionDate, powerConsumption, placement, smartHomeValidator);
        super.setId(IDENTIFIER + ++counterOfInstances);
        this.setMaximumLumen(maximumLumen);
        this.lightingMode = LightingMode.OFF;
        this.brightnessReductionPercentageMap = new EnumMap<>(LightingMode.class);
    }

    public LightingMode getLightingMode() {
        return lightingMode;
    }

    public BigDecimal getMaximumLumen() {
        return maximumLumen;
    }

    public Map<LightingMode, BigDecimal> getBrightnessReductionPercentageMap() {
        return brightnessReductionPercentageMap;
    }

    public void setLightingMode(LightingMode lightingMode) {
        super.getSmartHomeValidator().validateLightingMode(lightingMode);
        this.lightingMode = lightingMode;
    }

    public void setMaximumLumen(BigDecimal maximumLumen) {
        super.getSmartHomeValidator().validateMaximumLumen(maximumLumen);
        this.maximumLumen = maximumLumen;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmartLight that = (SmartLight) o;
        return lightingMode == that.lightingMode && Objects.equals(maximumLumen, that.maximumLumen) &&
            Objects.equals(brightnessReductionPercentageMap, that.brightnessReductionPercentageMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lightingMode, maximumLumen, brightnessReductionPercentageMap);
    }
}