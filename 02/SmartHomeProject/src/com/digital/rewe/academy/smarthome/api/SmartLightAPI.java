package com.digital.rewe.academy.smarthome.api;

import com.digital.rewe.academy.smarthome.LightingMode;

import java.math.BigDecimal;

/**
 * The SmartLightAPI is an interface to be implemented by all kings of smart lights that provides
 * methods to interact with a smart light device.
 * It allows retrieving the lumen output for a given lighting mode and sets the current
 * lighting mode of the smart light.
 */
public interface SmartLightAPI {

        /**
        * Retrieves the lumen output for a given lighting mode.
        * The method also sets the current lighting mode of the smart light.
        *
        * @param lightingMode the lighting mode for which to calculate the lumen output
        * @return the lumen output as a BigDecimal
        */
       public BigDecimal getLumenOutput(LightingMode lightingMode);
}