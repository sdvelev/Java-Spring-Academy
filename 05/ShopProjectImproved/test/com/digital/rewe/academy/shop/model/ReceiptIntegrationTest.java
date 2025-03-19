package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReceiptIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private Cashier cashierMock = Mockito.mock(Cashier.class);

    @Test
    void testSetCashierServingWithNullValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6, new ReceiptValidator());

        assertThrows(NullValueException.class, () -> myReceipt.setCashierServing(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetTotalPricePositiveCase() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6, new ReceiptValidator());
        BigDecimal newTotalPrice = BigDecimal.valueOf(16).setScale(2, RoundingMode.HALF_UP);
        myReceipt.setTotalPrice(newTotalPrice);

        assertEquals(newTotalPrice, myReceipt.getTotalPrice(),
            "Updated total price of the receipt is expected but the old value is returned");
    }

    @Test
    void testSetTotalPriceWithNullValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6, new ReceiptValidator());

        assertThrows(NullValueException.class, () -> myReceipt.setTotalPrice(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetTotalPriceWithNegativeValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6, new ReceiptValidator());

        assertThrows(NegativeValueException.class, () -> myReceipt.setTotalPrice(BigDecimal.valueOf(-16)),
            "NegativeValueException expected to be thrown when negative value is given");
    }
}