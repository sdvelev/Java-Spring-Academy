package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeviceIntegrationTest {

    private SmartHomeValidator smartHomeValidator;
    private Device device;

    @BeforeEach
    void setUp() {
        smartHomeValidator = new SmartHomeValidator();
        device = new Device("Sample model", LocalDate.of(2023, 10, 30),
            BigDecimal.valueOf(100), Placement.MOVABLE, smartHomeValidator);
    }

    @Test
    void testSetModelPositiveCase() {
        String newModel = "Sample model 2";
        device.setModel(newModel);

        assertEquals(newModel, device.getModel(),
            "The model of the device is expected to change but remains the old one");
    }

    @Test
    void testSetModelWithNullValue() {
        assertThrows(NullValueException.class, () -> device.setModel(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetModelWithBlankValue() {
        assertThrows(EmptyValueException.class, () -> device.setModel("         "),
            "EmptyValueException expected to be thrown when blank value is given");
    }

    @Test
    void testSetProductionDatePositiveCase() {
        LocalDate newProductionDate = LocalDate.of(2024, 1, 1);
        device.setProductionDate(newProductionDate);

        assertEquals(newProductionDate, device.getProductionDate(),
            "The production date of the device is expected to change but remains the old one");
    }

    @Test
    void testSetProductionDateWithNullValue() {
        assertThrows(NullValueException.class, () -> device.setProductionDate(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetPowerConsumptionPositiveCase() {
        BigDecimal newPowerConsumption = BigDecimal.valueOf(60);
        device.setPowerConsumption(newPowerConsumption);

        assertEquals(newPowerConsumption, device.getPowerConsumption(),
            "The power consumption of the device is expected to change but remains the old one");
    }

    @Test
    void testSetPowerConsumptionWithNegativeValue() {
        assertThrows(NegativeValueException.class, () -> device.setPowerConsumption(BigDecimal.valueOf(-60)),
            "NegativeValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetPlacementPositiveCase() {
        Placement newPlacement = Placement.BUILT_IN;
        device.setPlacement(newPlacement);

        assertEquals(newPlacement, device.getPlacement(),
            "The placement type of the device is expected to change but remains the old one");
    }

    @Test
    void testSetPlacementWithNullValue() {
        assertThrows(NullValueException.class, () -> device.setPlacement(null),
            "NullValueException expected to be thrown when null value is given");
    }
}