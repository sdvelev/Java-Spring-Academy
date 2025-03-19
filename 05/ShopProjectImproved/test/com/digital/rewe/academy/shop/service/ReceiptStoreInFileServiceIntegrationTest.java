package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptStoreInFileServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;
    private static final String RECEIPT_DIRECTORY = "." + File.separator + "receipts" + File.separator;
    private static final String FILE_NAME_PREFIX = "RECEIPT_";
    private static final String FILE_NAME_TEXT_SUFFIX = ".txt";

    private static final String RECEIPT_ATTRIBUTE_ID = "Receipt ID: ";
    private static final String RECEIPT_ATTRIBUTE_SHOP_NAME = "Shop Name: ";
    private static final String RECEIPT_ATTRIBUTE_CASH_DESK_NUMBER = "Cash Desk Number: ";
    private static final String RECEIPT_ATTRIBUTE_CASHIER_ID = "Cashier ID: ";
    private static final String RECEIPT_ATTRIBUTE_CASHIER_NAME = "Cashier Name: ";
    private static final String RECEIPT_ATTRIBUTE_DATE_OF_PURCHASE = "Date of Purchase: ";
    private static final String RECEIPT_ATTRIBUTE_GOOD_UNIT_PRICE = "Unit Price: ";
    private static final String RECEIPT_ATTRIBUTE_GOOD_QUANTITY = "Quantity: ";
    private static final String RECEIPT_ATTRIBUTE_TOTAL_AMOUNT = "Total Amount: ";

    private ReceiptValidator receiptValidator;
    private CashierValidator cashierValidator;
    private ReceiptStoreInFileService receiptStoreInFileService;
    private ReceiptGoodsService receiptGoodsService;

    @BeforeEach
    void setUp() {
        receiptValidator = new ReceiptValidator();
        cashierValidator = new CashierValidator();
        receiptStoreInFileService = new ReceiptStoreInFileService(receiptValidator);
        receiptGoodsService = new ReceiptGoodsService(receiptValidator);
    }

    @Test
    void testStoreInFileIntegrationSuccessfully() throws IOException {
        Cashier cashier = new Cashier(12L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        Receipt myReceipt = new Receipt(0, "My shop", cashier, 6, receiptValidator);

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodsMap = new LinkedHashMap<>();
        expectedGoodsMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.6)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.));
        expectedGoodsMap.put("Oranges 1KG", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(2.2)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 3.6));

        receiptGoodsService.addGoods(myReceipt, expectedGoodsMap);
        myReceipt.setTotalPrice(BigDecimal.valueOf(12.72));

        receiptStoreInFileService.storeInFile(myReceipt);

        List<String> receiptTextList;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(RECEIPT_DIRECTORY +
            FILE_NAME_PREFIX + myReceipt.getId() + FILE_NAME_TEXT_SUFFIX))) {
            receiptTextList = bufferedReader.lines().toList();

            assertEquals(RECEIPT_ATTRIBUTE_ID + "0", receiptTextList.get(0),
                "The receipt IDs after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_SHOP_NAME + "My shop", receiptTextList.get(1),
                "The receipt shop names after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_CASH_DESK_NUMBER + "6", receiptTextList.get(2),
                "The receipt cash desk numbers after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_CASHIER_ID + "12", receiptTextList.get(3),
                "The receipt cashier IDs after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_CASHIER_NAME + "Mark", receiptTextList.get(4),
                "The receipt cashier IDs after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_DATE_OF_PURCHASE + LocalDate.now(), receiptTextList.get(5),
                "The receipt purchase dates after storing in txt file are expected to be the same but they are not");

            assertEquals("", receiptTextList.get(6),
                "Empty line is expected but is not present");

            int counter = 1;
            assertEquals(counter++ + ". Water 1L", receiptTextList.get(7),
                "The receipt first item names after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_GOOD_UNIT_PRICE + "1.60", receiptTextList.get(8),
                "The receipt first item unit prices after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_GOOD_QUANTITY + "3.0", receiptTextList.get(9),
                "The receipt first item quantity after storing in txt file are expected to be the same but they are not");

            assertEquals(counter++ + ". Oranges 1KG", receiptTextList.get(10),
                "The receipt second item names after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_GOOD_UNIT_PRICE + "2.20", receiptTextList.get(11),
                "The receipt second item unit prices after storing in txt file are expected to be the same but they are not");

            assertEquals(RECEIPT_ATTRIBUTE_GOOD_QUANTITY + "3.6", receiptTextList.get(12),
                "The receipt second item quantity after storing in txt file are expected to be the same but they are not");

            assertEquals("", receiptTextList.get(13),
                "Empty line is expected but is not present");

            assertEquals(RECEIPT_ATTRIBUTE_TOTAL_AMOUNT + myReceipt.getTotalPrice(), receiptTextList.get(14),
                "The receipt total amounts after storing in txt file are expected to be the same but they are not");
        }
    }
}