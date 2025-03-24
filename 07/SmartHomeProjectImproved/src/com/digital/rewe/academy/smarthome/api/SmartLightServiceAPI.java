package com.digital.rewe.academy.smarthome.api;

import com.digital.rewe.academy.smarthome.enums.LightingMode;
import com.digital.rewe.academy.smarthome.exception.BrightnessReductionPercentageMissingException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.SmartLight;

import java.math.BigDecimal;

/**
 * The SmartLightServiceAPI is an interface that provides
 * methods to interact with a smart light device.
 * It allows retrieving the lumen output for a given lighting mode and sets the current
 * lighting mode of the smart light.
 */
public interface SmartLightServiceAPI {

    /**
     * Adds a brightness reduction percentage for a given lighting mode to the specified smart light.
     *
     * @param smartLight the smart light to which the brightness reduction percentage will be added
     * @param lightingMode the lighting mode for which the brightness reduction percentage is being set
     * @param percentage the brightness reduction percentage to be added
     * @throws NullValueException if any of the parameters are null
     * @throws NegativeValueException if the percentage is negative
     */
     void addBrightnessReductionPercentage(SmartLight smartLight, LightingMode lightingMode, BigDecimal percentage);

    /**
     * Retrieves the lumen output for a given lighting mode.
     * The method also sets the current lighting mode of the smart light.
     * Exceptions that are handled by the method:
     * NullValueException - if the lighting mode is null;
     *
     * @param smartLight the smart light for which the method is to be executed
     * @param lightingMode the lighting mode for which to calculate the lumen output
     * @throws NullValueException if any of the parameters is null
     * @throws BrightnessReductionPercentageMissingException if there is not a defined percentage reduction for the provided lighting mode
     * @return the lumen output as a BigDecimal
     */
     BigDecimal getLumenOutput(SmartLight smartLight, LightingMode lightingMode);
}