package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Device {

    private String id;
    private String model;
    private LocalDate productionDate;
    private BigDecimal powerConsumption;
    private Placement placement;

    public Device(String model, LocalDate productionDate, BigDecimal powerConsumption, Placement placement) {
        validateConstructorFields(model, productionDate, powerConsumption, placement);
        this.model = model;
        this.productionDate = productionDate;
        this.powerConsumption = powerConsumption;
        this.placement = placement;
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

    protected void setId(String id) {
        this.id = id;
    }

    public void setModel(String model) {
        validateModel(model);
        this.model = model;
    }

    public void setProductionDate(LocalDate productionDate) {
        validateProductionDate(productionDate);
        this.productionDate = productionDate;
    }

    public void setPowerConsumption(BigDecimal powerConsumption) {
        validatePowerConsumption(powerConsumption);
        this.powerConsumption = powerConsumption;
    }

    public void setPlacement(Placement placement) {
        validatePlacement(placement);
        this.placement = placement;
    }

    private void validateConstructorFields(String model, LocalDate productionDate, BigDecimal powerConsumption,
                                           Placement placement) {
        validateModel(model);
        validateProductionDate(productionDate);
        validatePowerConsumption(powerConsumption);
        validatePlacement(placement);
    }

    private void validateModel(String model) {
        if (model == null) {
            throw new NullValueException("The model argument cannot be null");
        }

        if (model.isBlank()) {
            throw new EmptyValueException("The model argument cannot be empty or blank");
        }
    }

    private void validateProductionDate(LocalDate productionDate) {
        if (productionDate == null) {
            throw new NullValueException("The production date argument cannot be null");
        }

        if (productionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The production date argument cannot be in the future");
        }
    }

    private void validatePowerConsumption(BigDecimal powerConsumption) {
        if (powerConsumption == null) {
            throw new NullValueException("The power consumption argument cannot be null");
        }

        if (powerConsumption.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The power consumption argument cannot be negative");
        }
    }

    private void validatePlacement(Placement placement) {
        if (placement == null) {
            throw new NullValueException("The placement argument cannot be null");
        }
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