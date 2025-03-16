package com.digital.rewe.academy.shop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Map;

public class ReceiptService {
    private static final String RECEIPT_DIRECTORY = "." + File.separator + "receipts" + File.separator;
    private static final String FILE_NAME_PREFIX = "RECEIPT_";
    private static final String FILE_NAME_TEXT_SUFFIX = ".txt";
    private static final String FILE_NAME_SERIALIZATION_SUFFIX = ".ser";
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

    public static void storeInFile(Receipt receiptToStore) throws IOException {
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
            throw new IOException("There is a problem working with files", e);
        }
    }

    public static void serializeReceipt(Receipt receiptToSerialize) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new
            FileOutputStream(RECEIPT_DIRECTORY + FILE_NAME_PREFIX + receiptToSerialize.getId() +
            FILE_NAME_SERIALIZATION_SUFFIX))) {

            objectOutputStream.writeObject(receiptToSerialize);
        } catch (IOException e) {
            throw new IOException("There is a problem working with files", e);
        }
    }

    public static Receipt deserializeReceipt(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Receipt) objectInputStream.readObject();
        }
    }
}