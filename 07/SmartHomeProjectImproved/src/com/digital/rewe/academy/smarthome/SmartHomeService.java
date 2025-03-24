package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.SmartHomeServiceAPI;
import com.digital.rewe.academy.smarthome.model.Device;
import com.digital.rewe.academy.smarthome.model.SmartHome;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;

public class SmartHomeService implements SmartHomeServiceAPI {

    private SmartHomeValidator smartHomeValidator;

    public SmartHomeService(SmartHomeValidator smartHomeValidator) {
        this.smartHomeValidator = smartHomeValidator;
    }

    @Override
    public void addDevice(SmartHome smartHome, Device deviceToAdd) {
        smartHomeValidator.validateSmartHome(smartHome);
        smartHomeValidator.validateDevice(deviceToAdd);
        smartHome.getDevices().add(deviceToAdd);
    }

    @Override
    public void removeDevice(SmartHome smartHome, Device deviceToRemove) {
        smartHomeValidator.validateSmartHome(smartHome);
        smartHomeValidator.validateDevice(deviceToRemove);
        smartHome.getDevices().remove(deviceToRemove);
    }
}