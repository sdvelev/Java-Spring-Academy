package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class GoodTest {

    @Test
    void testSetNamePositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        String newName = "Mineral Water 1L";
        water.setName(newName);

        assertEquals(newName, water.getName(),
            "The name of the good is expected to change but remains the old one");
    }

    @Test
    void testSetNameWithNullValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NullValueException.class, () -> water.setName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetNameWithEmptyValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(EmptyValueException.class, () -> water.setName(""),
            "EmptyValueException expected to be thrown when name of good is empty");
    }

    @Test
    void testSetGoodCategoryPositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        GoodCategory newCategory = GoodCategory.FOOD;
        water.setGoodCategory(newCategory);

        assertEquals(newCategory, water.getGoodCategory(),
            "The category of the good is expected to change but remains the old one");
    }

    @Test
    void testSetGoodCategoryWithNullValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NullValueException.class, () -> water.setGoodCategory(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetGoodUnitPositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.KILOGRAM, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        GoodUnit newUnit = GoodUnit.LITER;
        water.setGoodUnit(newUnit);

        assertEquals(newUnit, water.getGoodUnit(),
            "The unit of the good is expected to change but remains the old one");
    }

    @Test
    void testSetGoodUnitWithNullValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NullValueException.class, () -> water.setGoodUnit(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetPurchasePricePositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        BigDecimal newPurchasePrice = BigDecimal.valueOf(1.80).setScale(2, RoundingMode.HALF_UP);
        water.setPurchasePrice(newPurchasePrice);

        assertEquals(newPurchasePrice, water.getPurchasePrice(),
            "The purchase price of the good is expected to change but remains the old one");
    }

    @Test
    void testSetPurchasePriceWithNullValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NullValueException.class, () -> water.setPurchasePrice(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetPurchasePriceWithNegativeValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NegativeValueException.class, () -> water.setPurchasePrice(BigDecimal.valueOf(-1.8)),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testSetExpiryDatePositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.KILOGRAM, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        LocalDate newExpiryDate = LocalDate.of(2025, 12, 12);
        water.setExpiryDate(newExpiryDate);

        assertEquals(newExpiryDate, water.getExpiryDate(),
            "The expiry date of the good is expected to change but remains the old one");
    }

    @Test
    void testSetExpiryDateWithNullValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NullValueException.class, () -> water.setExpiryDate(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetDeliveryPercentagePositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.KILOGRAM, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        BigDecimal newDeliveryPercentage = BigDecimal.valueOf(0.09);
        water.setDeliveryPercentage(newDeliveryPercentage);

        assertEquals(newDeliveryPercentage, water.getDeliveryPercentage(),
            "The delivery percentage of the good is expected to change but remains the old one");
    }

    @Test
    void testSetDeliveryPercentageWithNullValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NullValueException.class, () -> water.setDeliveryPercentage(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetDeliveryPercentageWithNegativeValue() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        assertThrows(NegativeValueException.class, () -> water.setDeliveryPercentage(BigDecimal.valueOf(-0.09)),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testIsExpirableGoodNonFood() {
        Good book = new Good(1, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        assertFalse(book.isExpirableGood(), "The good object is a book and is expected to be non-expirable good");
    }

    @Test
    void testEqualsAreNotEqual() {
        Good bookPaperBack = new Good(1, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Good bookHardCover = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(29.9), BigDecimal.valueOf(0.1));

        assertNotEquals(bookPaperBack, bookHardCover,
            "The books are not expected to be equal but the actual value is positive");
    }
}