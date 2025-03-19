package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.GoodValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GoodUnitTest {

    @Mock
    private GoodValidator goodValidatorMock = Mockito.mock(GoodValidator.class);

    private Good water;

    @BeforeEach
    void setUp() {
        water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidatorMock, LocalDate.of(2025, 12, 1));
    }

    @Test
    void testSetNamePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(goodValidatorMock).validateName(Mockito.anyString());
        String newName = "Mineral Water 1L";
        water.setName(newName);
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("name"),
                ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(water);
        Mockito.verify(goodValidatorMock, Mockito.times(2)).validateName(Mockito.anyString());
        assertEquals(newName, actualName, "The name of the good is expected to change but remains the old one");
    }

    @Test
    void testSetNameWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(goodValidatorMock).validateName(Mockito.anyString());
        String newName = "              ";
        assertThrows(EmptyValueException.class, () -> water.setName(newName),
            "EmptyValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("name"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualName = (String) field.get(water);
        assertEquals("Water 1L", actualName, "The name of the good is not expected to change but it has");
    }

    @Test
    void testSetGoodCategoryPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(goodValidatorMock).validateGoodCategory(Mockito.any());
        GoodCategory newGoodCategory = GoodCategory.NON_FOOD;
        water.setGoodCategory(newGoodCategory);
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("goodCategory"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        GoodCategory actualGoodCategory = (GoodCategory) field.get(water);
        Mockito.verify(goodValidatorMock, Mockito.times(2)).validateGoodCategory(Mockito.any());
        assertEquals(newGoodCategory, actualGoodCategory, "The category of the good is expected to change but remains the old one");
    }

    @Test
    void testSetGoodCategoryWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(goodValidatorMock).validateGoodCategory(Mockito.any());
        GoodCategory newGoodCategory = null;
        assertThrows(NullValueException.class, () -> water.setGoodCategory(newGoodCategory),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("goodCategory"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        GoodCategory actualGoodCategory = (GoodCategory) field.get(water);
        assertEquals(GoodCategory.FOOD, actualGoodCategory, "The category of the good is not expected to change but it has");
    }

    @Test
    void testSetGoodUnitPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(goodValidatorMock).validateGoodUnit(Mockito.any());
        GoodUnit newGoodUnit = GoodUnit.PIECE;
        water.setGoodUnit(newGoodUnit);
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("goodUnit"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        GoodUnit actualGoodUnit = (GoodUnit) field.get(water);
        Mockito.verify(goodValidatorMock, Mockito.times(2)).validateGoodUnit(Mockito.any());
        assertEquals(newGoodUnit, actualGoodUnit, "The unit of the good is expected to change but remains the old one");
    }

    @Test
    void testSetGoodUnitWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(goodValidatorMock).validateGoodUnit(Mockito.any());
        GoodUnit newGoodUnit = null;
        assertThrows(NullValueException.class, () -> water.setGoodUnit(newGoodUnit),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("goodUnit"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        GoodUnit actualGoodUnit = (GoodUnit) field.get(water);
        assertEquals(GoodUnit.LITER, actualGoodUnit, "The unit of the good is not expected to change but it has");
    }

    @Test
    void testSetPurchasePricePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(goodValidatorMock).validatePurchasePrice(Mockito.any());
        BigDecimal newPurchasePrice = BigDecimal.valueOf(1.8);
        water.setPurchasePrice(newPurchasePrice);
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("purchasePrice"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualPurchasePrice = (BigDecimal) field.get(water);
        Mockito.verify(goodValidatorMock, Mockito.times(2)).validatePurchasePrice(Mockito.any());
        assertEquals(newPurchasePrice, actualPurchasePrice,
            "The purchase price of the good is expected to change but remains the old one");
    }

    @Test
    void testSetPurchasePriceWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(goodValidatorMock).validatePurchasePrice(Mockito.any());
        BigDecimal newPurchasePrice = BigDecimal.valueOf(-6);
        assertThrows(NegativeValueException.class, () -> water.setPurchasePrice(newPurchasePrice),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("purchasePrice"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualPurchasePrice = (BigDecimal) field.get(water);
        assertEquals(BigDecimal.valueOf(1.6), actualPurchasePrice,
            "The purchase price of the good is not expected to change but it has");
    }

    @Test
    void testSetExpiryDatePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(goodValidatorMock).validateExpiryDate(Mockito.any());
        LocalDate newExpiryDate = LocalDate.of(2025, 10, 30);
        water.setExpiryDate(newExpiryDate);
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("expiryDate"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LocalDate actualExpiryDate = (LocalDate) field.get(water);
        Mockito.verify(goodValidatorMock, Mockito.times(2)).validateExpiryDate(Mockito.any());
        assertEquals(newExpiryDate, actualExpiryDate,
            "The expiry date of the good is expected to change but remains the old one");
    }

    @Test
    void testSetExpiryDateWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(goodValidatorMock).validateExpiryDate(Mockito.any());
        LocalDate newExpiryDate = null;
        assertThrows(NullValueException.class, () -> water.setExpiryDate(newExpiryDate),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("expiryDate"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LocalDate actualExpiryDate = (LocalDate) field.get(water);
        assertEquals(LocalDate.of(2025, 12, 1), actualExpiryDate,
            "The expiry date of the good is not expected to change but it has");
    }

    @Test
    void testSetDeliveryPercentagePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(goodValidatorMock).validateDeliveryPercentage(Mockito.any());
        BigDecimal newDeliveryPercentage = BigDecimal.valueOf(0.6);
        water.setDeliveryPercentage(newDeliveryPercentage);
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("deliveryPercentage"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualDeliveryPercentage = (BigDecimal) field.get(water);
        Mockito.verify(goodValidatorMock, Mockito.times(2)).validateDeliveryPercentage(Mockito.any());
        assertEquals(newDeliveryPercentage, actualDeliveryPercentage,
            "The delivery percentage of the good is expected to change but remains the old one");
    }

    @Test
    void testSetDeliveryPercentageWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(goodValidatorMock).validateDeliveryPercentage(Mockito.any());
        BigDecimal newDeliveryPercentage = BigDecimal.valueOf(-0.6);
        assertThrows(NegativeValueException.class, () -> water.setDeliveryPercentage(newDeliveryPercentage),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Good.class, f -> f.getName().equals("deliveryPercentage"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualDeliveryPercentage = (BigDecimal) field.get(water);
        assertEquals(BigDecimal.valueOf(0.1), actualDeliveryPercentage,
            "The delivery percentage of the good is not expected to change but it has");
    }
}