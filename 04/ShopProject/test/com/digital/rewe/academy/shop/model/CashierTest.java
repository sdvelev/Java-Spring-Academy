package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class CashierTest {

    @Test
    void testSetCashierNamePositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));
        String newName = "John";
        cashier.setName(newName);

        assertEquals(newName, cashier.getName(),
            "The name of the cashier is expected to change but remains the old one");
    }

    @Test
    void testSetCashierNameWithNullValue() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(NullValueException.class, () -> cashier.setName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetCashierNameWithBlankValue() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(EmptyValueException.class, () -> cashier.setName("         "),
            "EmptyValueException expected to be thrown when blank value is given");
    }

    @Test
    void testSetShopNamePositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));
        String newShopName = "New Shop";
        cashier.setShopName(newShopName);

        assertEquals(newShopName, cashier.getShopName(),
            "The shop name of the cashier is expected to change but remains the old one");
    }

    @Test
    void testSetShopNameWithNullValue() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(NullValueException.class, () -> cashier.setShopName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetShopNameWithBlankValue() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(EmptyValueException.class, () -> cashier.setShopName("         "),
            "EmptyValueException expected to be thrown when blank value is given");
    }

    @Test
    void testSetCashierSalaryPositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));
        BigDecimal newSalary = BigDecimal.valueOf(3600).setScale(2, RoundingMode.HALF_UP);
        cashier.setSalary(newSalary);

        assertEquals(newSalary, cashier.getSalary(),
            "The salary of the cashier is expected to change but remains the old one");
    }

    @Test
    void testSetCashierSalaryWithNullValue() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(NullValueException.class, () -> cashier.setSalary(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetCashierSalaryWithNegativeValue() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(NegativeValueException.class, () -> cashier.setSalary(BigDecimal.valueOf(-3600)),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testEqualsAreEqual() {
        Cashier firstCashier = new Cashier(1, "Mark", "My first shop", BigDecimal.valueOf(2600));
        Cashier secondCashier = new Cashier(1, "Mark", "My second shop", BigDecimal.valueOf(3600));

        assertEquals(firstCashier, secondCashier,
            "The cashiers are expected to be equal but the actual value is negative");
    }
}