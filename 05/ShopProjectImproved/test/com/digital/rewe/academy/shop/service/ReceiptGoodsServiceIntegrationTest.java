package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ReceiptGoodsServiceAPI;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReceiptGoodsServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private Cashier cashierMock = Mockito.mock(Cashier.class);

    private ReceiptValidator receiptValidator;
    private ReceiptGoodsServiceAPI receiptGoodsService;

    private Receipt myReceipt;

    @BeforeEach
    public void setup() {
        receiptValidator = new ReceiptValidator();
        receiptGoodsService = new ReceiptGoodsService(receiptValidator);
        myReceipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidator);
    }

    @Test
    void testAddGoodsPositiveCase() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidator);

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.));
        expectedGoodsMap.put("Orange 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.6));

        receiptGoodsService.addGoods(myReceipt, expectedGoodsMap);

        assertIterableEquals(expectedGoodsMap.entrySet(), myReceipt.getGoodWithQuantityMap().entrySet(),
            "The actual map of goods is not the same as the expected");
    }

    @Test
    void testRemoveGoodPositiveCase() {
        Receipt myReceipt = new Receipt(1, "My shop", cashierMock, 6, receiptValidator);

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6), 3.));
        expectedGoodsMap.put("Orange 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2), 3.6));

        receiptGoodsService.addGoods(myReceipt, expectedGoodsMap);
        receiptGoodsService.removeGood(myReceipt, "Water 1L");

        assertEquals(1, myReceipt.getGoodWithQuantityMap().size(),
            "Only one type of good is expected to remain");
    }

    @Test
    void testRemoveGoodWithNullValue() {
        assertThrows(NullValueException.class, () -> receiptGoodsService.removeGood(myReceipt, null),
            "NullValueException expected to be thrown when null value is given");
    }
}