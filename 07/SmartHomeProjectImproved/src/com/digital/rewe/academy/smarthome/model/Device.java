package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Device {
    private static final String IDENTIFIER = "DEVICE-";

    private static long counterOfInstances = 0;

    private SmartHomeValidator smartHomeValidator;

    private String id;
    private String model;
    private LocalDate productionDate;
    private BigDecimal powerConsumption;
    private Placement placement;

    public Device(String model, LocalDate productionDate, BigDecimal powerConsumption, Placement placement,
                  SmartHomeValidator smartHomeValidator) {
        this.smartHomeValidator = smartHomeValidator;
        this.setId(IDENTIFIER + counterOfInstances++);
        this.setModel(model);
        this.setProductionDate(productionDate);
        this.setPowerConsumption(powerConsumption);
        this.setPlacement(placement);
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public BigDecimal getPowerConsumption() {
        return powerConsumption;
    }

    public Placement getPlacement() {
        return placement;
    }

    protected SmartHomeValidator getSmartHomeValidator() {
        return smartHomeValidator;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public void setModel(String model) {
        smartHomeValidator.validateModel(model);
        this.model = model;
    }

    public void setProductionDate(LocalDate productionDate) {
        smartHomeValidator.validateProductionDate(productionDate);
        this.productionDate = productionDate;
    }

    public void setPowerConsumption(BigDecimal powerConsumption) {
        smartHomeValidator.validatePowerConsumption(powerConsumption);
        this.powerConsumption = powerConsumption;
    }

    public void setPlacement(Placement placement) {
        smartHomeValidator.validatePlacement(placement);
        this.placement = placement;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}