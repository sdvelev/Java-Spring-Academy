package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.enums.Placement;
import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.exception.NegativeValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeviceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private SmartHomeValidator smartHomeValidatorMock;
    private Device device;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        device = new Device("Sample model", LocalDate.of(2023, 10, 30),
            BigDecimal.valueOf(100), Placement.MOVABLE, smartHomeValidatorMock);
    }

    @Test
    void testSetModelPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validateModel(Mockito.anyString());
        String newModel = "Sample model 2";
        device.setModel(newModel);
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("model"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualModel = (String) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateModel(Mockito.anyString());
        assertEquals(newModel, actualModel, "The model of the device is expected to change but remains the old one");
    }

    @Test
    void testSetModelWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateModel(Mockito.any());
        String newModel = null;
        assertThrows(NullValueException.class, () -> device.setModel(newModel),
            "NullValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("model"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualModel = (String) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateModel(Mockito.any());
        assertEquals("Sample model", actualModel, "The model of the device is not expected to change");
    }

    @Test
    void testSetModelWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(smartHomeValidatorMock).validateModel(Mockito.anyString());
        String newModel = "               ";
        assertThrows(EmptyValueException.class, () -> device.setModel(newModel),
            "EmptyValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("model"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualModel = (String) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateModel(Mockito.anyString());
        assertEquals("Sample model", actualModel, "The model of the device is not expected to change");
    }

    @Test
    void testSetProductionDatePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validateProductionDate(Mockito.any(LocalDate.class));
        LocalDate newProductionDate = LocalDate.of(2025, 1, 1);
        device.setProductionDate(newProductionDate);
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("productionDate"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LocalDate actualProductionDate = (LocalDate) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateProductionDate(Mockito.any());
        assertEquals(newProductionDate, actualProductionDate, "The production date of the device is expected to change but remains the old one");
    }

    @Test
    void testSetProductionDateWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateProductionDate(Mockito.any());
        LocalDate newProductionDate = null;
        assertThrows(NullValueException.class, () -> device.setProductionDate(newProductionDate),
            "NullValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("productionDate"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LocalDate actualProductionDate = (LocalDate) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateProductionDate(Mockito.any());
        assertEquals(LocalDate.of(2023, 10, 30), actualProductionDate, "The production date of the device is not expected to change");
    }

    @Test
    void testSetPowerConsumptionPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validatePowerConsumption(Mockito.any());
        BigDecimal newPowerConsumption = BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        device.setPowerConsumption(newPowerConsumption);
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("powerConsumption"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualPowerConsumption = (BigDecimal) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validatePowerConsumption(Mockito.any());
        assertEquals(newPowerConsumption, actualPowerConsumption, "The power consumption of the device is expected to change but remains the old one");
    }

    @Test
    void testSetPowerConsumptionWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(smartHomeValidatorMock).validatePowerConsumption(Mockito.any());
        BigDecimal newPowerConsumption = BigDecimal.valueOf(-60);
        assertThrows(NegativeValueException.class, () -> device.setPowerConsumption(newPowerConsumption),
        "NegativeValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("powerConsumption"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualPowerConsumption = (BigDecimal) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validatePowerConsumption(Mockito.any());
        assertEquals(BigDecimal.valueOf(100), actualPowerConsumption, "The power consumption of the device is not expected to change but it has");
    }

    @Test
    void testSetPlacementPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validatePlacement(Mockito.any());
        Placement newPlacement = Placement.BUILT_IN;
        device.setPlacement(newPlacement);
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("placement"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Placement actualPlacement = (Placement) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validatePlacement(Mockito.any());
        assertEquals(newPlacement, actualPlacement, "The placement of the device is expected to change but remains the old one");
    }

    @Test
    void testSetPlacementWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validatePlacement(Mockito.any());
        Placement newPlacement = null;
        assertThrows(NullValueException.class, () -> device.setPlacement(newPlacement),
            "NullValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(Device.class, f -> f.getName().equals("placement"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Placement actualPlacement = (Placement) field.get(device);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validatePlacement(Mockito.any());
        assertEquals(Placement.MOVABLE, actualPlacement, "The placement of the device is not expected to change");
    }
}