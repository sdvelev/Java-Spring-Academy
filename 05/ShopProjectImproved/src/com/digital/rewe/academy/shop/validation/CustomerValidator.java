package com.digital.rewe.academy.shop.validation;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Good;

import java.math.BigDecimal;

public class CustomerValidator {
    public void validateAvailableMoney(BigDecimal availableMoney) {
        if (availableMoney == null) {
            throw new NullValueException("The available money of the customer cannot be null");
        }

        if (availableMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The available money of the customer cannot be negative");
        }
    }

    public void validateGood(Good good) {
        if (good == null) {
            throw new NullValueException("The provided good cannot be null");
        }
    }

    public void validateAmount(double amount) {
        if (amount < 0) {
            throw new NegativeValueException("The amount of the good in basket cannot be negative");
        }
    }
}