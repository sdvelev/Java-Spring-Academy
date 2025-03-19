package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShopIntegrationTest {
    
    @Test
    void testSetNamePositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1),
            new ShopValidator());
        String newShopName = "My new shop";
        shop.setName(newShopName);

        assertEquals(newShopName, shop.getName(), "Shop name is updated but not changed");
    }

    @Test
    void testSetNameWithNullValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), new ShopValidator());

        assertThrows(NullValueException.class,  () -> shop.setName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetNameWithEmptyValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), new ShopValidator());

        assertThrows(EmptyValueException.class,  () -> shop.setName(""),
            "EmptyValueException expected to be thrown when name of shop is empty");
    }

    @Test
    void testSetDaysBeforeExpiryDateDiscountWithNegativeValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), new ShopValidator());

        assertThrows(NegativeValueException.class,  () -> shop.setDaysBeforeExpiryDateDiscount(-6),
            "NegativeValueException expected to be thrown when days before expiry date discount is negative");
    }

    @Test
    void testSetDiscountExpiryDatePercentageWithNegativeValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), new ShopValidator());

        assertThrows(NegativeValueException.class,  () -> shop.setDiscountExpiryDatePercentage(BigDecimal.valueOf(-0.1)),
            "NegativeValueException expected to be thrown when discount expiry date percentage is negative");
    }
}