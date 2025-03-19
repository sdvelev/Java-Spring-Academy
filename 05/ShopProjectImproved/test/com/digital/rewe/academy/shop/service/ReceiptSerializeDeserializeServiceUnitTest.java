package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ReceiptSerializeDeserializeServiceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private ReceiptValidator receiptValidatorMock;
    @Mock
    private Cashier cashierMock;
    @Mock
    private Receipt myReceiptMock;
    private ReceiptSerializeDeserializeService receiptSerializeDeserializeService;

    @BeforeEach
    void setUp() {
        receiptValidatorMock = Mockito.mock(ReceiptValidator.class);
        cashierMock = Mockito.mock(Cashier.class);
        myReceiptMock = Mockito.mock(Receipt.class, Mockito.withSettings().serializable());
        receiptSerializeDeserializeService = new ReceiptSerializeDeserializeService(receiptValidatorMock);
    }

    @Test
    void testSerializeDeserializeReceiptUnitSuccessfully() throws IOException, ClassNotFoundException {
        Mockito.doNothing().when(receiptValidatorMock).validateReceipt(myReceiptMock);
        Mockito.when(myReceiptMock.getId()).thenReturn(1L);
        Mockito.when(myReceiptMock.getShopName()).thenReturn("My shop");
        Mockito.when(myReceiptMock.getCashDeskNumber()).thenReturn(6);
        Mockito.when(myReceiptMock.getCashierServing()).thenReturn(cashierMock);
        Mockito.when(cashierMock.getId()).thenReturn(12L);
        Mockito.when(cashierMock.getName()).thenReturn("Mark");
        Mockito.when(myReceiptMock.getDateOfPurchase()).thenReturn(LocalDate.now());

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.));
        expectedGoodsMap.put("Oranges 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.6));

        Mockito.when(myReceiptMock.getGoodWithQuantityMap()).thenReturn(expectedGoodsMap);

        Mockito.when(myReceiptMock.getTotalPrice()).thenReturn(BigDecimal.valueOf(12.72)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        receiptSerializeDeserializeService.serializeReceipt(myReceiptMock);

        Receipt deserializedReceipt = receiptSerializeDeserializeService.deserializeReceipt("RECEIPT_1.ser");

        assertEquals(deserializedReceipt.getId(), myReceiptMock.getId(),
            "IDs of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getShopName(), myReceiptMock.getShopName(),
            "The shop names of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getCashierServing(), myReceiptMock.getCashierServing(),
            "The cashiers of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getCashDeskNumber(), myReceiptMock.getCashDeskNumber(),
            "The cash desk numbers of receipts are expected to be the same but they are not");

        assertEquals(LocalDate.now(), myReceiptMock.getDateOfPurchase(),
            "The date of purchase must be the current date");

        assertEquals(deserializedReceipt.getDateOfPurchase(), myReceiptMock.getDateOfPurchase(),
            "The dates of purchase numbers of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getTotalPrice(), myReceiptMock.getTotalPrice(),
            "The total price of receipts are expected to be the same but they are not");

        assertIterableEquals(deserializedReceipt.getGoodWithQuantityMap().entrySet(),
            myReceiptMock.getGoodWithQuantityMap().entrySet(),
            "The goods in the receipt are expected to be the same but they are not");
    }
}