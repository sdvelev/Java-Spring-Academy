package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.exception.EmptyValueException;

import java.util.HashSet;
import java.util.Set;

public class SmartHome {

    private final long id;
    private String ownerName;
    private Set<Device> devices;

    public SmartHome(long id, String ownerName) {
        this.id = id;
        validateOwnerName(ownerName);
        this.ownerName = ownerName;
        this.devices = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        validateOwnerName(ownerName);
        this.ownerName = ownerName;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device deviceToAdd) {
        this.devices.add(deviceToAdd);
    }

    public void removeDevice(Device deviceToRemove) {
        this.devices.remove(deviceToRemove);
    }

    private void validateOwnerName(String ownerName) {
        if (ownerName == null) {
            throw new NullPointerException("The name of the owner cannot be null");
        }

        if (ownerName.isBlank()) {
            throw new EmptyValueException("The name of the owner cannot be empty or blank");
        }
    }
}