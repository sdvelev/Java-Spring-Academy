package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShopUnitTest {

    @Mock
    private ShopValidator shopValidatorMock = Mockito.mock(ShopValidator.class);

    private Shop shop;

    @BeforeEach
    void setUp() {
        shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), shopValidatorMock);
    }

    @Test
    void testSetNamePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(shopValidatorMock).validateShopName(Mockito.anyString());
        String newName = "My new shop";
        shop.setName(newName);
        Field field = ReflectionUtils.findFields(Shop.class, f -> f.getName().equals("name"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(shop);
        Mockito.verify(shopValidatorMock, Mockito.times(2)).validateShopName(Mockito.anyString());
        assertEquals(newName, actualName, "The name of the shop is expected to change but remains the old one");
    }

    @Test
    void testSetNameWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(shopValidatorMock).validateShopName(Mockito.anyString());
        String newName = "              ";
        assertThrows(EmptyValueException.class, () -> shop.setName(newName),
            "EmptyValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Shop.class, f -> f.getName().equals("name"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(shop);
        assertEquals("My shop", actualName, "The name of the shop is not expected to change but it has");
    }

    @Test
    void testSetDaysBeforeExpiryDateDiscountPositiveCase() throws IllegalAccessException {
        int newDays = 8;
        Mockito.doNothing().when(shopValidatorMock).validateDaysBeforeExpiryDateDiscount(newDays);
        shop.setDaysBeforeExpiryDateDiscount(newDays);
        Field field = ReflectionUtils.findFields(Shop.class, f -> f.getName().equals("daysBeforeExpiryDateDiscount"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        int actualDays = (int) field.get(shop);
        Mockito.verify(shopValidatorMock, Mockito.times(1)).validateDaysBeforeExpiryDateDiscount(7);
        Mockito.verify(shopValidatorMock, Mockito.times(1)).validateDaysBeforeExpiryDateDiscount(8);
        assertEquals(newDays, actualDays,
            "The days before expiry date for discount of the good is expected to change but remains the old one");
    }

    @Test
    void testSetDaysBeforeExpiryDateDiscountWithNegativeValue() throws IllegalAccessException {
        int newDays = -6;
        Mockito.doThrow(NegativeValueException.class).when(shopValidatorMock).validateDaysBeforeExpiryDateDiscount(newDays);
        assertThrows(NegativeValueException.class, () -> shop.setDaysBeforeExpiryDateDiscount(newDays),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Shop.class, f -> f.getName().equals("daysBeforeExpiryDateDiscount"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        int actualDays = (int) field.get(shop);
        assertEquals(7, actualDays,
            "The days before expiry date for discount of the good is not expected to change but it has");
    }

    @Test
    void testSetDiscountExpiryDatePercentagePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(shopValidatorMock).validateDiscountExpiryDatePercentage(Mockito.any());
        BigDecimal newDiscount = BigDecimal.valueOf(0.2);
        shop.setDiscountExpiryDatePercentage(newDiscount);
        Field field = ReflectionUtils.findFields(Shop.class, f -> f.getName().equals("discountExpiryDatePercentage"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualDiscount = (BigDecimal) field.get(shop);
        Mockito.verify(shopValidatorMock, Mockito.times(2)).validateDiscountExpiryDatePercentage(Mockito.any());
        assertEquals(newDiscount, actualDiscount,
            "The discount expiry date percentage of the good is expected to change but remains the old one");
    }

    @Test
    void testSetDiscountExpiryDatePercentageWithNegativeValue() throws IllegalAccessException {
        BigDecimal newDiscount = BigDecimal.valueOf(-0.6);
        Mockito.doThrow(NegativeValueException.class).when(shopValidatorMock).validateDiscountExpiryDatePercentage(newDiscount);
        assertThrows(NegativeValueException.class, () -> shop.setDiscountExpiryDatePercentage(newDiscount),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Shop.class, f -> f.getName().equals("discountExpiryDatePercentage"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualDiscount = (BigDecimal) field.get(shop);
        assertEquals(BigDecimal.valueOf(0.1), actualDiscount,
            "The discount expiry date percentage of the good is not expected to change but it has");
    }
}