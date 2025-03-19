package com.digital.rewe.academy.shop.validation;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Receipt;

import java.math.BigDecimal;

public class ReceiptValidator {
    public void validateShopName(String shopName) {
        if (shopName == null) {
            throw new NullValueException("The provided name of the shop cannot be null");
        }

        if (shopName.isBlank()) {
            throw new EmptyValueException("The provided name of the shop cannot be empty or blank");
        }
    }

    public void validateCashierServing(Cashier cashierServing) {
        if (cashierServing == null) {
            throw new NullValueException("The cashier cannot be null");
        }
    }

    public void validateCashDeskNumber(int cashDeskNumber) {
        if (cashDeskNumber < 0) {
            throw new NegativeValueException("The cash desk number cannot be negative");
        }
    }

    public void validateGoodName(String goodName) {
        if (goodName == null) {
            throw new NullValueException("The provided name of the good cannot be null");
        }

        if (goodName.isBlank()) {
            throw new EmptyValueException("The provided name of the good cannot be empty or blank");
        }
    }

    public void validateTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null) {
            throw new NullValueException("The price argument cannot be null");
        }

        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The price cannot be negative");
        }
    }

    public void validateQuantity(double quantity) {
        if (quantity < 0) {
            throw new NegativeValueException("The quantity of the provided good cannot be negative");
        }
    }

    public void validateReceipt(Receipt receipt) {
        if (receipt == null) {
            throw new NullValueException("The provided receipt cannot be null");
        }
    }

    public void validateFileName(String fileName) {
        if (fileName == null) {
            throw new NullValueException("The provided file name cannot be null");
        }

        if (fileName.isBlank()) {
            throw new EmptyValueException("The provided file name cannot be empty or blank");
        }
    }
}