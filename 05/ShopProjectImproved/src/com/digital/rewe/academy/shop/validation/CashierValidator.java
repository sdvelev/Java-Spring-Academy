package com.digital.rewe.academy.shop.validation;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.math.BigDecimal;

public class CashierValidator {
    public void validateName(String name) {
        if (name == null) {
            throw new NullValueException("The name of the cashier cannot be null");
        }

        if (name.isBlank()) {
            throw new EmptyValueException("The name of the cashier cannot be empty or blank");
        }
    }

    public void validateShopName(String name) {
        if (name == null) {
            throw new NullValueException("The name of the shop cannot be null");
        }

        if (name.isBlank()) {
            throw new EmptyValueException("The name of the shop cannot be empty or blank");
        }
    }

    public void validateSalary(BigDecimal salary) {
        if (salary == null) {
            throw new NullValueException("The salary of the cashier cannot be null");
        }

        if (salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The salary of the cashier cannot be negative");
        }
    }
}