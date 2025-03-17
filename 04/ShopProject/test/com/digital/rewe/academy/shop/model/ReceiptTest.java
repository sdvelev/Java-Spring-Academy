package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private Cashier cashierMock = Mockito.mock(Cashier.class);

    @Test
    void testSetCashierServingWithNullValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);

        assertThrows(NullValueException.class, () -> myReceipt.setCashierServing(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetTotalPricePositiveCase() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);
        BigDecimal newTotalPrice = BigDecimal.valueOf(16).setScale(2, RoundingMode.HALF_UP);
        myReceipt.setTotalPrice(newTotalPrice);

        assertEquals(newTotalPrice, myReceipt.getTotalPrice(),
            "Updated total price of the receipt is expected but the old value is returned");
    }

    @Test
    void testSetTotalPriceWithNullValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);

        assertThrows(NullValueException.class, () -> myReceipt.setTotalPrice(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetTotalPriceWithNegativeValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);

        assertThrows(NegativeValueException.class, () -> myReceipt.setTotalPrice(BigDecimal.valueOf(-16)),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testAddGoodsPositiveCase() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.));
        expectedGoodsMap.put("Orange 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.6));

        myReceipt.addGoods(expectedGoodsMap);

        assertIterableEquals(expectedGoodsMap.entrySet(), myReceipt.getGoodWithQuantityMap().entrySet(),
        "The actual map of goods is not the same as the expected");
    }

    @Test
    void testRemoveGoodPositiveCase() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6), 3.));
        expectedGoodsMap.put("Orange 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2), 3.6));

        myReceipt.addGoods(expectedGoodsMap);
        myReceipt.removeGood("Water 1L");

        assertEquals(1, myReceipt.getGoodWithQuantityMap().size(),
            "Only one type of good is expected to remain");
    }

    @Test
    void testRemoveGoodWithNullValue() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6);

        assertThrows(NullValueException.class, () -> myReceipt.removeGood(null),
            "NullValueException expected to be thrown when null value is given");
    }
}