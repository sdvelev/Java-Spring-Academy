package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ReceiptStoreInFileServiceAPI;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class ReceiptStoreInFileService implements ReceiptStoreInFileServiceAPI {
    private static final String RECEIPT_DIRECTORY = "." + File.separator + "receipts" + File.separator;
    private static final String FILE_NAME_PREFIX = "RECEIPT_";
    private static final String FILE_NAME_TEXT_SUFFIX = ".txt";
    private static final String GOODS_DELIMITER = ". ";

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

    public ReceiptStoreInFileService(ReceiptValidator receiptValidator) {
        this.receiptValidator = receiptValidator;
    }

    @Override
    public void storeInFile(Receipt receiptToStore) throws IOException {
        receiptValidator.validateReceipt(receiptToStore);
        try (BufferedWriter bufferedWriter = new BufferedWriter(
            new FileWriter(RECEIPT_DIRECTORY + FILE_NAME_PREFIX + receiptToStore.getId() + FILE_NAME_TEXT_SUFFIX))) {

            bufferedWriter.write(RECEIPT_ATTRIBUTE_ID + receiptToStore.getId() + System.lineSeparator());
            bufferedWriter.write(RECEIPT_ATTRIBUTE_SHOP_NAME + receiptToStore.getShopName() + System.lineSeparator());
            bufferedWriter.write(RECEIPT_ATTRIBUTE_CASH_DESK_NUMBER + receiptToStore.getCashDeskNumber() + System.lineSeparator());
            bufferedWriter.write(RECEIPT_ATTRIBUTE_CASHIER_ID + receiptToStore.getCashierServing().getId() + System.lineSeparator());
            bufferedWriter.write(RECEIPT_ATTRIBUTE_CASHIER_NAME + receiptToStore.getCashierServing().getName() + System.lineSeparator());
            bufferedWriter.write(RECEIPT_ATTRIBUTE_DATE_OF_PURCHASE + receiptToStore.getDateOfPurchase() + System.lineSeparator());

            bufferedWriter.write(System.lineSeparator());

            int counter = 1;
            for (Map.Entry<String, Map.Entry<BigDecimal, Double>> currentEntry :
                receiptToStore.getGoodWithQuantityMap().entrySet()) {
                bufferedWriter.write(counter++ + GOODS_DELIMITER + currentEntry.getKey() + System.lineSeparator());
                bufferedWriter.write( RECEIPT_ATTRIBUTE_GOOD_UNIT_PRICE + currentEntry.getValue().getKey() +
                    System.lineSeparator());
                bufferedWriter.write(RECEIPT_ATTRIBUTE_GOOD_QUANTITY + currentEntry.getValue().getValue() +
                    System.lineSeparator());
            }

            bufferedWriter.write(System.lineSeparator());
            bufferedWriter.write(RECEIPT_ATTRIBUTE_TOTAL_AMOUNT + receiptToStore.getTotalPrice() +
                System.lineSeparator());
        } catch (IOException e) {
            throw new IOException("There is a problem storing receipt in txt file", e);
        }
    }
}