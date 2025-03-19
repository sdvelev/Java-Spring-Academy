package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ReceiptSerializeDeserializeServiceAPI;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReceiptSerializeDeserializeService implements ReceiptSerializeDeserializeServiceAPI {
    private static final String RECEIPT_DIRECTORY = "." + File.separator + "receipts" + File.separator;
    private static final String FILE_NAME_PREFIX = "RECEIPT_";
    private static final String FILE_NAME_SERIALIZATION_SUFFIX = ".ser";

    private ReceiptValidator receiptValidator;

    public ReceiptSerializeDeserializeService(ReceiptValidator receiptValidator) {
        this.receiptValidator = receiptValidator;
    }

    @Override
    public void serializeReceipt(Receipt receiptToSerialize) throws IOException {
        receiptValidator.validateReceipt(receiptToSerialize);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new
            FileOutputStream(RECEIPT_DIRECTORY + FILE_NAME_PREFIX + receiptToSerialize.getId() +
            FILE_NAME_SERIALIZATION_SUFFIX))) {

            objectOutputStream.writeObject(receiptToSerialize);
        } catch (IOException e) {
            throw new IOException("There is a problem serializing receipt in ser file", e);
        }
    }

    @Override
    public Receipt deserializeReceipt(String fileName) throws IOException, ClassNotFoundException {
        receiptValidator.validateFileName(fileName);
        try (
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(RECEIPT_DIRECTORY + fileName))) {
            return (Receipt) objectInputStream.readObject();
        } catch (IOException e) {
            throw new IOException("There is a problem deserializing receipt from ser file", e);
        }
    }
}