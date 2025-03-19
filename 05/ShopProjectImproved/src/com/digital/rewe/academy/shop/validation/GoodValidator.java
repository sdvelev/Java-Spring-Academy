package com.digital.rewe.academy.shop.validation;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GoodValidator {

    public void validateName(String name) {
        if (name == null) {
            throw new NullValueException("The name of the good cannot be null");
        }

        if (name.isBlank()) {
            throw new EmptyValueException("The name of the good cannot be empty or blank");
        }
    }

    public void validateGoodCategory(GoodCategory goodCategory) {
        if (goodCategory == null) {
            throw new NullValueException("The category of the good cannot be null");
        }
    }

    public void validateGoodUnit(GoodUnit goodUnit) {
        if (goodUnit == null) {
            throw new NullValueException("The unit of the good cannot be null");
        }
    }

    public void validatePurchasePrice(BigDecimal purchasePrice) {
        if (purchasePrice == null) {
            throw new NullValueException("The purchase price of the good cannot be null");
        }

        if (purchasePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The purchase price of the good cannot be negative");
        }
    }

    public void validateDeliveryPercentage(BigDecimal deliveryPercentage) {
        if (deliveryPercentage == null) {
            throw new NullValueException("The delivery percentage of the good cannot be null");
        }

        if (deliveryPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The delivery percentage of the good cannot be negative");
        }
    }

    public void validateExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new NullValueException("The expiry date of the good (if provided) cannot be null");
        }
    }
}