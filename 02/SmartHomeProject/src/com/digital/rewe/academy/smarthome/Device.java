package com.digital.rewe.academy.smarthome;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Device {

    private String id;
    private String model;
    private LocalDate productionDate;
    private BigDecimal powerConsumption;
    private Placement placement;

    public Device(String model, LocalDate productionDate, BigDecimal powerConsumption, Placement placement) {
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
        this.model = model;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public void setPowerConsumption(BigDecimal powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }
}