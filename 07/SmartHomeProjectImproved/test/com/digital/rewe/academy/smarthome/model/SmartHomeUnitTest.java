package com.digital.rewe.academy.smarthome.model;

import com.digital.rewe.academy.smarthome.exception.EmptyValueException;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;


import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartHomeUnitTest {

    private SmartHomeValidator smartHomeValidatorMock;
    private SmartHome smartHome;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        smartHome = new SmartHome(1, "Stefan", smartHomeValidatorMock);
    }

    @Test
    void testSetOwnerNamePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(smartHomeValidatorMock).validateOwnerName(Mockito.anyString());
        String newOwner = "Stefan Velev";
        smartHome.setOwnerName(newOwner);
        Field field = ReflectionUtils.findFields(SmartHome.class, f -> f.getName().equals("ownerName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualOwner = (String) field.get(smartHome);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateOwnerName(Mockito.anyString());
        assertEquals(newOwner, actualOwner, "The owner of the smart home is expected to change but remains the old one");
    }

    @Test
    void testSetOwnerNameWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateOwnerName(Mockito.any());
        String newOwner = null;
        assertThrows(NullValueException.class, () -> smartHome.setOwnerName(newOwner),
            "NullValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(SmartHome.class, f -> f.getName().equals("ownerName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualOwner = (String) field.get(smartHome);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateOwnerName(Mockito.any());
        assertEquals("Stefan", actualOwner, "The owner of the smart home is not expected to change");
    }

    @Test
    void testSetOwnerNameWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(smartHomeValidatorMock).validateOwnerName(Mockito.any());
        String newOwner = "               ";
        assertThrows(EmptyValueException.class, () -> smartHome.setOwnerName(newOwner),
            "EmptyValueException is expected but not thrown");
        Field field = ReflectionUtils.findFields(SmartHome.class, f -> f.getName().equals("ownerName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualOwner = (String) field.get(smartHome);
        Mockito.verify(smartHomeValidatorMock, Mockito.times(2)).validateOwnerName(Mockito.any());
        assertEquals("Stefan", actualOwner, "The owner of the smart home is not expected to change");
    }
}