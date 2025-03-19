package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.CustomerValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerIntegrationTest {

    @Test
    void testSetAvailableMoneyPositiveCase() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        BigDecimal newAvailableMoney = BigDecimal.valueOf(80).setScale(2, RoundingMode.HALF_UP);
        customer.setAvailableMoney(newAvailableMoney);

        assertEquals(newAvailableMoney, customer.getAvailableMoney(),
            "The available money of the customer is expected to change but remains the old one");
    }

    @Test
    void testSetAvailableMoneyWithNullValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        assertThrows(NullValueException.class, () -> customer.setAvailableMoney(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetAvailableMoneyNegativeValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        assertThrows(NegativeValueException.class, () -> customer.setAvailableMoney(BigDecimal.valueOf(-60)),
            "NegativeValueException expected to be thrown when negative value is given");
    }
}