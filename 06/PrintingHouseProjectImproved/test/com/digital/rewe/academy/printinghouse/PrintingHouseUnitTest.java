package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.EmptyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseUnitTest {

    @Mock
    private PrintingHouseValidator printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);

    private PrintingHouse printingHouse;

    @BeforeEach
    void setUp() {
        printingHouse = new PrintingHouse(1, "My printing house", printingHouseValidatorMock);
    }

    @Test
    void testSetHouseNamePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(printingHouseValidatorMock).validateHouseName(Mockito.anyString());
        String newName = "My new printing house";
        printingHouse.setHouseName(newName);
        Field field = ReflectionUtils.findFields(PrintingHouse.class, f -> f.getName().equals("houseName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(printingHouse);
        Mockito.verify(printingHouseValidatorMock, Mockito.times(2)).validateHouseName(Mockito.anyString());
        assertEquals(newName, actualName, "The name of the printing house is expected to change but remains the old one");
    }

    @Test
    void testSetHouseNameWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(printingHouseValidatorMock).validateHouseName(Mockito.anyString());
        String newName = "              ";
        assertThrows(EmptyValueException.class, () -> printingHouse.setHouseName(newName),
            "EmptyValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(PrintingHouse.class, f -> f.getName().equals("houseName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(printingHouse);
        assertEquals("My printing house", actualName,
            "The name of the printing house is not expected to change but it has");
    }
}