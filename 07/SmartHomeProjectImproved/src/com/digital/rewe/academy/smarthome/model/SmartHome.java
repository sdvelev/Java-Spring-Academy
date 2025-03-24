package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

import java.util.HashSet;
import java.util.Set;

public class SmartHome {

    private final long id;
    private String ownerName;
    private final Set<Device> devices;

    private SmartHomeValidator smartHomeValidator;

    public SmartHome(long id, String ownerName, SmartHomeValidator smartHomeValidator) {
        this.smartHomeValidator = smartHomeValidator;
        this.id = id;
        this.setOwnerName(ownerName);
        this.devices = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        smartHomeValidator.validateOwnerName(ownerName);
        this.ownerName = ownerName;
    }

    public Set<Device> getDevices() {
        return devices;
    }
}