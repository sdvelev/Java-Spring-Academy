package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Receipt;

import java.io.IOException;

/**
 * Interface for operations related to receipt serialization and deserialization.
 */
public interface ReceiptSerializeDeserializeServiceAPI {

    /**
     * Serializes the given receipt.
     *
     * @param receipt the receipt to serialize
     * @throws NullValueException if the receipt is null
     * @throws IOException if an I/O error occurs during serialization
     */
    void serializeReceipt(Receipt receipt) throws IOException;

    /**
     * Deserializes a receipt from the given file.
     *
     * @param fileName the name of the file from which to deserialize the receipt
     * @return the deserialized receipt
     * @throws NullValueException if the file name is null
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    Receipt deserializeReceipt(String fileName) throws IOException, ClassNotFoundException;
}