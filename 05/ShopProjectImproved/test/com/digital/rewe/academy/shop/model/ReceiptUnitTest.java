package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReceiptUnitTest {
    @Mock
    private ReceiptValidator receiptValidatorMock = Mockito.mock(ReceiptValidator.class);

    @Mock
    Cashier cashierMock = Mockito.mock(Cashier.class);


    @Test
    void testSetCashierServingPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(receiptValidatorMock).validateCashierServing(Mockito.any(Cashier.class));
        Receipt receipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidatorMock);
        Cashier newCashierMock =  Mockito.mock(Cashier.class);
        receipt.setCashierServing(newCashierMock);

        Field field = ReflectionUtils.findFields(Receipt.class, f -> f.getName().equals("cashierServing"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Cashier actualCashierServing = (Cashier) field.get(receipt);
        Mockito.verify(receiptValidatorMock, Mockito.times(2)).validateCashierServing(Mockito.any(Cashier.class));
        assertEquals(newCashierMock, actualCashierServing,
            "The cashier of the receipt is expected to change but remains the old one");
    }

    @Test
    void testSetCashierServingWithNullValue() throws IllegalAccessException {
        Mockito.doNothing().when(receiptValidatorMock).validateCashierServing(cashierMock);
        Receipt receipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidatorMock);
        Cashier newCashier = null;
        Mockito.doThrow(NullValueException.class).when(receiptValidatorMock).validateCashierServing(newCashier);
        assertThrows(NullValueException.class, () -> receipt.setCashierServing(newCashier),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Receipt.class, f -> f.getName().equals("cashierServing"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Cashier actualCashier = (Cashier) field.get(receipt);
        assertEquals(cashierMock, actualCashier, "The cashier of the receipt is not expected to change but they have");
    }

    @Test
    void testSetTotalPricePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(receiptValidatorMock).validateCashierServing(cashierMock);
        Receipt receipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidatorMock);
        Mockito.doNothing().when(receiptValidatorMock).validateTotalPrice(Mockito.any());
        BigDecimal newTotalPrice = BigDecimal.valueOf(6.6);
        receipt.setTotalPrice(newTotalPrice);
        Field field = ReflectionUtils.findFields(Receipt.class, f -> f.getName().equals("totalPrice"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualTotalPrice = (BigDecimal) field.get(receipt);
        Mockito.verify(receiptValidatorMock, Mockito.times(1)).validateTotalPrice(Mockito.any());
        assertEquals(newTotalPrice, actualTotalPrice,
            "The total price of the receipt is expected to change but remains the old one");
    }

    @Test
    void testSetTotalPriceWithNegativeValue() throws IllegalAccessException {
        Mockito.doNothing().when(receiptValidatorMock).validateCashierServing(cashierMock);
        Receipt receipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidatorMock);
        Mockito.doThrow(NegativeValueException.class).when(receiptValidatorMock).validateTotalPrice(Mockito.any());
        BigDecimal newTotalPrice = BigDecimal.valueOf(-6);
        assertThrows(NegativeValueException.class, () -> receipt.setTotalPrice(newTotalPrice),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Receipt.class, f -> f.getName().equals("totalPrice"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualTotalPrice = (BigDecimal) field.get(receipt);
        assertEquals(BigDecimal.ZERO, actualTotalPrice,
            "The total price of the good is not expected to change but it has");
    }
}