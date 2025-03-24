package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartHomeIntegrationTest {

    private SmartHomeValidator smartHomeValidator;
    private SmartHome smartHome;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        smartHome = new SmartHome(1, "Stefan", smartHomeValidator);
    }

    @Test
    void testSetOwnerNamePositiveCase() {
        String newOwner = "Stefan Velev";
        smartHome.setOwnerName(newOwner);

        assertEquals(newOwner, smartHome.getOwnerName(),
            "The owner of the smart home is expected to change but remains the old one");
    }

    @Test
    void testSetModelWithNullValue() {
        assertThrows(NullValueException.class, () -> smartHome.setOwnerName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetModelWithBlankValue() {
        assertThrows(EmptyValueException.class, () -> smartHome.setOwnerName("         "),
            "EmptyValueException expected to be thrown when blank value is given");
    }
}