package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashDeskUnitTest {

    @Mock
    private CashDeskValidator cashDeskValidator = Mockito.mock(CashDeskValidator.class);

    @Mock
    private Cashier cashierMock = Mockito.mock(Cashier.class);

    private CashDesk cashDesk;

    @BeforeEach
    void setUp() {
        cashDesk = new CashDesk(1, 5, cashDeskValidator);
    }

    @Test
    void testSetCashDeskNumberPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(cashDeskValidator).validateCashDeskNumber(Mockito.anyInt());
        int newCashDeskNumber = 6;
        cashDesk.setCashDeskNumber(newCashDeskNumber);
        Field field = ReflectionUtils.findFields(CashDesk.class, f -> f.getName().equals("cashDeskNumber"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        int actualCashDeskNumber = (int) field.get(cashDesk);
        Mockito.verify(cashDeskValidator, Mockito.times(1)).validateCashDeskNumber(5);
        Mockito.verify(cashDeskValidator, Mockito.times(1)).validateCashDeskNumber(newCashDeskNumber);
        assertEquals(newCashDeskNumber, actualCashDeskNumber,
            "The number of the cash desk is expected to change but remains the old one");
    }

    @Test
    void testSetCashDeskNumberWithNegativeValue() throws IllegalAccessException {
        int newCashDeskNumber = -6;
        Mockito.doThrow(NegativeValueException.class).when(cashDeskValidator).validateCashDeskNumber(newCashDeskNumber);
        assertThrows(NegativeValueException.class, () -> cashDesk.setCashDeskNumber(newCashDeskNumber),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(CashDesk.class, f -> f.getName().equals("cashDeskNumber"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        int actualCashDeskNumber = (int) field.get(cashDesk);
        assertEquals(5, actualCashDeskNumber,
            "The number of the cash desk is not expected to change but it has");
    }

    @Test
    void testSetWorkingCashierPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(cashDeskValidator).validateCashier(cashierMock);
        cashDesk.setWorkingCashier(cashierMock);

        Field field = ReflectionUtils.findFields(CashDesk.class, f -> f.getName().equals("workingCashier"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Cashier actualWorkingCashier = (Cashier) field.get(cashDesk);
        Mockito.verify(cashDeskValidator, Mockito.times(1)).validateCashier(cashierMock);
        assertEquals(cashierMock, actualWorkingCashier,
            "The cashier at the cash desk is expected to change but remains the old one");
    }

    @Test
    void testSetWorkingCashierWithNullValue() throws IllegalAccessException {
        Mockito.doNothing().when(cashDeskValidator).validateCashier(cashierMock);
        Cashier newCashier = null;
        Mockito.doThrow(NullValueException.class).when(cashDeskValidator).validateCashier(newCashier);
        assertThrows(NullValueException.class, () -> cashDesk.setWorkingCashier(newCashier),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(CashDesk.class, f -> f.getName().equals("workingCashier"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Cashier actualWorkingCashier = (Cashier) field.get(cashDesk);
        assertNull(actualWorkingCashier, "The cashier at the cash desk is not expected to change but they have");
    }
}