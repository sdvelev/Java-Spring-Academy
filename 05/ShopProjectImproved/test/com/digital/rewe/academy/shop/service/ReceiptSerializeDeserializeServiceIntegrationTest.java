package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ReceiptGoodsServiceAPI;
import com.digital.rewe.academy.shop.api.ReceiptSerializeDeserializeServiceAPI;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ReceiptSerializeDeserializeServiceIntegrationTest {
    private ReceiptValidator receiptValidator;
    private CashierValidator cashierValidator;
    private ReceiptSerializeDeserializeServiceAPI receiptSerializeDeserializeService;
    private ReceiptGoodsServiceAPI receiptGoodsService;

    @BeforeEach
    void setUp() {
        receiptValidator = new ReceiptValidator();
        cashierValidator = new CashierValidator();
        receiptSerializeDeserializeService = new ReceiptSerializeDeserializeService(receiptValidator);
        receiptGoodsService = new ReceiptGoodsService(receiptValidator);
    }

    @Test
    void testSerializeDeserializeReceiptIntegrationSuccessfully() throws IOException, ClassNotFoundException {
        Cashier cashier = new Cashier(12L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        Receipt myReceipt = new Receipt(0, "My shop", cashier, 6, receiptValidator);

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6), 3.));
        expectedGoodsMap.put("Oranges 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2), 3.6));

        receiptGoodsService.addGoods(myReceipt, expectedGoodsMap);
        myReceipt.setTotalPrice(BigDecimal.valueOf(12.72));

        receiptSerializeDeserializeService.serializeReceipt(myReceipt);

        Receipt deserializedReceipt = receiptSerializeDeserializeService.deserializeReceipt("RECEIPT_0.ser");

        assertEquals(deserializedReceipt.getId(), myReceipt.getId(),
            "IDs of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getShopName(), myReceipt.getShopName(),
            "The shop names of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getCashierServing(), myReceipt.getCashierServing(),
            "The cashiers of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getCashDeskNumber(), myReceipt.getCashDeskNumber(),
            "The cash desk numbers of receipts are expected to be the same but they are not");

        assertEquals(LocalDate.now(), myReceipt.getDateOfPurchase(),
            "The date of purchase must be the current date");

        assertEquals(deserializedReceipt.getDateOfPurchase(), myReceipt.getDateOfPurchase(),
            "The dates of purchase numbers of receipts are expected to be the same but they are not");

        assertEquals(deserializedReceipt.getTotalPrice(), myReceipt.getTotalPrice(),
            "The total price of receipts are expected to be the same but they are not");

        assertIterableEquals(deserializedReceipt.getGoodWithQuantityMap().entrySet(),
            myReceipt.getGoodWithQuantityMap().entrySet(),
            "The goods in the receipt are expected to be the same but they are not");
    }
}