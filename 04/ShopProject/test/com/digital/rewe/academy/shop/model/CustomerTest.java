package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.Shop;
import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerTest {

    private Shop shopMock = Mockito.mock(Shop.class);

    @Test
    void testSetAvailableMoneyPositiveCase() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        BigDecimal newAvailableMoney = BigDecimal.valueOf(80).setScale(2, RoundingMode.HALF_UP);
        customer.setAvailableMoney(newAvailableMoney);

        assertEquals(newAvailableMoney, customer.getAvailableMoney(),
            "The available money of the customer is expected to change but remains the old one");
    }

    @Test
    void testSetAvailableMoneyWithNullValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        assertThrows(NullValueException.class, () -> customer.setAvailableMoney(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetAvailableMoneyNegativeValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        assertThrows(NegativeValueException.class, () -> customer.setAvailableMoney(BigDecimal.valueOf(-60)),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testAddToBasketPositiveCase() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        customer.addToBasket(water, 3.);
        Map<Good, Double> expectedBasket = Map.of(water, 3.);

        assertIterableEquals(expectedBasket.entrySet(), customer.getBasket().entrySet(),
            "The expected basket after adding is not the same as the actual");
        verify(shopMock).removeGood(water, 3.);
    }

    @Test
    void testAddToBasketWithGoodNullValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);

        assertThrows(NullValueException.class, () -> customer.addToBasket(null, 6),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testAddToBasketWithNegativeQuantity() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NegativeValueException.class, () -> customer.addToBasket(water, -6),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void removeFromBasketPositiveCaseEverythingRemoved() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        customer.addToBasket(water, 6);
        verify(shopMock).removeGood(water, 6.);

        customer.removeFromBasket(water, 6.);
        assertTrue(customer.getBasket().isEmpty(),
            "The basket of the customer is expected to be empty but it is not");

        verify(shopMock).returnGood(water, 6.);
    }

    @Test
    void removeFromBasketPositiveCasePartiallyRemoved() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        customer.addToBasket(water, 6);
        verify(shopMock).removeGood(water, 6.);

        customer.removeFromBasket(water, 4.);
        assertEquals(2, customer.getBasket().get(water),
            "Two elements are expected to be in the basket of the customer but they are not");

        verify(shopMock).returnGood(water, 4.);
    }

    @Test
    void testRemoveFromBasketWithGoodNullValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);

        assertThrows(NullValueException.class, () -> customer.removeFromBasket(null, 6),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testRemoveFromBasketWithNegativeQuantity() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NegativeValueException.class, () -> customer.removeFromBasket(water, -6),
            "NegativeValueException expected to be thrown when negative value is given");
    }
}