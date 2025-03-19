package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashierUnitTest {
    @Mock
    private CashierValidator cashierValidatorMock = Mockito.mock(CashierValidator.class);

    private Cashier cashier;

    @BeforeEach
    void setUp() {
        cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidatorMock);
    }

    @Test
    void testSetNamePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(cashierValidatorMock).validateName(Mockito.anyString());
        String newName = "Steven";
        cashier.setName(newName);
        Field field = ReflectionUtils.findFields(Cashier.class, f -> f.getName().equals("name"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(cashier);
        Mockito.verify(cashierValidatorMock, Mockito.times(2)).validateName(Mockito.anyString());
        assertEquals(newName, actualName, "The name of the cashier is expected to change but remains the old one");
    }

    @Test
    void testSetNameWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(cashierValidatorMock).validateName(Mockito.anyString());
        String newName = "              ";
        assertThrows(EmptyValueException.class, () -> cashier.setName(newName),
            "EmptyValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Cashier.class, f -> f.getName().equals("name"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(cashier);
        assertEquals("Mark", actualName, "The name of the cashier is not expected to change but it has");
    }

    @Test
    void testSetShopNamePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(cashierValidatorMock).validateShopName(Mockito.anyString());
        String newShopName = "My new shop";
        cashier.setShopName(newShopName);
        Field field = ReflectionUtils.findFields(Cashier.class, f -> f.getName().equals("shopName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(cashier);
        Mockito.verify(cashierValidatorMock, Mockito.times(2)).validateShopName(Mockito.anyString());
        assertEquals(newShopName, actualName, "The shop name of the cashier is expected to change but remains the old one");
    }

    @Test
    void testSetShopNameWithEmptyValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(cashierValidatorMock).validateShopName(Mockito.anyString());
        String newShopName = "";
        assertThrows(EmptyValueException.class, () -> cashier.setShopName(newShopName),
            "EmptyValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Cashier.class, f -> f.getName().equals("shopName"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(cashier);
        assertEquals("My shop", actualName, "The shop name of the cashier is not expected to change but it has");
    }

    @Test
    void testSetSalaryPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(cashierValidatorMock).validateSalary(Mockito.any());
        BigDecimal newSalary = BigDecimal.valueOf(6000);
        cashier.setSalary(newSalary);
        Field field = ReflectionUtils.findFields(Cashier.class, f -> f.getName().equals("salary"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualSalary = (BigDecimal) field.get(cashier);
        Mockito.verify(cashierValidatorMock, Mockito.times(2)).validateSalary(Mockito.any());
        assertEquals(newSalary, actualSalary,
            "The salary of the cashier is expected to change but remains the old one");
    }

    @Test
    void testSetSalaryWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(cashierValidatorMock).validateSalary(Mockito.any());
        BigDecimal newSalary = BigDecimal.valueOf(-6666);
        assertThrows(NegativeValueException.class, () -> cashier.setSalary(newSalary),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Cashier.class, f -> f.getName().equals("salary"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualSalary = (BigDecimal) field.get(cashier);
        assertEquals(BigDecimal.valueOf(2600), actualSalary,
            "The salary of the cashier is not expected to change but it has");
    }
}