package com.digital.rewe.academy.smarthome.api;

import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.Device;
import com.digital.rewe.academy.smarthome.model.SmartHome;

public interface SmartHomeServiceAPI {

    /**
     * Adds a device to the specified smart home.
     *
     * @param smartHome the smart home to which the device will be added
     * @param deviceToAdd the device to be added
     * @throws NullValueException if the smart home or device is null
     */
    void addDevice(SmartHome smartHome, Device deviceToAdd);

    /**
     * Removes a device from the specified smart home.
     *
     * @param smartHome the smart home from which the device will be removed
     * @param deviceToRemove the device to be removed
     * @throws NullValueException if the smart home or device is null
     */
    void removeDevice(SmartHome smartHome, Device deviceToRemove);
}