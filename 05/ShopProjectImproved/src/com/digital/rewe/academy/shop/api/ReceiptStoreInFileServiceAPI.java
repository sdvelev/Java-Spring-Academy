package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Receipt;

import java.io.IOException;

/**
 * Interface for operations related to storing receipts in files.
 */
public interface ReceiptStoreInFileServiceAPI {

    /**
     * Stores the given receipt in a file following the convention: RECEIPT_{ID}.txt
     *
     * @param receipt the receipt to store
     * @throws NullValueException if the receipt is null
     * @throws IOException if an I/O error occurs during storing
     */
    void storeInFile(Receipt receipt) throws IOException;
}