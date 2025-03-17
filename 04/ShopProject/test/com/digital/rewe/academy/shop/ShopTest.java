package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;
import com.digital.rewe.academy.shop.exception.NoSuchGoodException;
import com.digital.rewe.academy.shop.exception.NoSuchQuantityException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ShopTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Test
    void testSetNamePositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        String newShopName = "My new shop";
        shop.setName(newShopName);

        assertEquals(newShopName, shop.getName(), "Shop name is updated but not changed");
    }

    @Test
    void testSetNameWithNullValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        assertThrows(NullValueException.class,  () -> shop.setName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetNameWithEmptyValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        assertThrows(EmptyValueException.class,  () -> shop.setName(""),
            "EmptyValueException expected to be thrown when name of shop is empty");
    }

    @Test
    void testSetDaysBeforeExpiryDateDiscountWithNegativeValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        assertThrows(NegativeValueException.class,  () -> shop.setDaysBeforeExpiryDateDiscount(-6),
            "NegativeValueException expected to be thrown when days before expiry date discount is negative");
    }

    @Test
    void testSetDiscountExpiryDatePercentageWithNegativeValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        assertThrows(NegativeValueException.class,  () -> shop.setDiscountExpiryDatePercentage(BigDecimal.valueOf(-0.1)),
            "NegativeValueException expected to be thrown when discount expiry date percentage is negative");
    }

    @Test
    void testAddMarkupPercentagePositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        shop.addMarkupPercentage(GoodCategory.FOOD, BigDecimal.valueOf(0.1));

        assertEquals(BigDecimal.valueOf(0.1), shop.getMarkupPercentage(GoodCategory.FOOD),
            "Actual markup percentage is not the same as the expected");
    }

    @Test
    void testAddMarkupPercentageWithGoodCategoryNullValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        assertThrows(NullValueException.class,  () -> shop.addMarkupPercentage(null, BigDecimal.valueOf(0.1)),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testAddMarkupPercentageWithPercentageNegativeValue() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        assertThrows(NegativeValueException.class,  () -> shop.addMarkupPercentage(GoodCategory.NON_FOOD, BigDecimal.valueOf(-0.1)),
            "NegativeValueException expected to be thrown when negative value for markup percentage is given");
    }

    @Test
    void testRemoveCashierPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        shop.addCashier(cashier);
        shop.removeCashier(cashier);

        assertTrue(shop.getCashiers().isEmpty(), "The cashier collection is expected to be empty but it is not");
    }

    @Test
    void testRemoveCashierUnknownCashier() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));

        assertThrows(NoSuchCashierException.class, () -> shop.removeCashier(cashier),
            "NoSuchCashierException is expected but not thrown");
    }

    @Test
    void testRemoveCashDeskPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        CashDesk cashDesk = new CashDesk(1, 5, shop);

        shop.addCashDesk(cashDesk);
        shop.removeCashDesk(cashDesk);

        assertTrue(shop.getCashDesks().isEmpty(), "The cash desk collection is expected to be empty but it is not");
    }

    @Test
    void testRemoveCashDeskUnknownCashDesk() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        CashDesk cashDesk = new CashDesk(1, 5, shop);

        assertThrows(NoSuchCashDeskException.class, () -> shop.removeCashDesk(cashDesk),
            "NoSuchCashDeskException is expected but not thrown");
    }

    @Test
    void testLoadNewGoodsPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        newGoodsMap.put(book, 1.);

        shop.loadNewGoods(newGoodsMap);

        assertEquals(BigDecimal.valueOf(32.45).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shop.getCostsForDay(LocalDate.now()), "The current daily costs are not the same as the expected");
        assertTrue(shop.getAvailableGoods().containsKey(water) && shop.getAvailableGoods().get(water).equals(6.),
            "Part of the expected goods with quantities are missing");
        assertTrue(shop.getAvailableGoods().containsKey(book) && shop.getAvailableGoods().get(book).equals(1.),
            "Part of the expected goods with quantities are missing");
    }

    @Test
    void testAddIncomePositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        shop.addIncome(BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shop.getIncomeForDay(LocalDate.now()), "The current daily income is not the same as the expected");
    }

    @Test
    void testRemoveGoodPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        newGoodsMap.put(book, 1.);

        shop.loadNewGoods(newGoodsMap);

        shop.removeGood(water, 2);
        shop.removeGood(book, 1);

        assertTrue(shop.getAvailableGoods().containsKey(water) && shop.getAvailableGoods().get(water).equals(4.),
            "Part of the expected goods with quantities are missing or incorrect number");
        assertFalse(shop.getAvailableGoods().containsKey(book),
            "Part of the expected goods with quantities are present but they are expected to miss");
    }

    @Test
    void testRemoveGoodNotEnoughQuantity() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        shop.loadNewGoods(newGoodsMap);

        assertThrows(NoSuchQuantityException.class, () -> shop.removeGood(water, 10),
            "NoSuchQuantityException is expected but not thrown");
    }

    @Test
    void testRemoveGoodNoSuchGood() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        shop.loadNewGoods(newGoodsMap);

        assertThrows(NoSuchGoodException.class, () -> shop.removeGood(book, 1),
            "NoSuchGoodException is expected but not thrown");
    }

    @Test
    void testReturnGoodPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        shop.loadNewGoods(newGoodsMap);

        shop.removeGood(water, 2.);
        shop.returnGood(water, 1.);

        assertTrue(shop.getAvailableGoods().containsKey(water) && shop.getAvailableGoods().get(water).equals(5.),
            "Part of the expected goods with quantities are missing or incorrect number");
    }

    private static boolean isEndOfTheMonth(LocalDate date) {
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }

    @Test
    void testGetCostsForDayPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));
        shop.addCashier(cashier);

        BigDecimal totalSalariesAmount = shop.getCashiers().stream()
            .map(Cashier::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        shop.loadNewGoods(newGoodsMap);

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(totalSalariesAmount.add(BigDecimal.valueOf(10.56).setScale(ROUNDING_SCALE_FACTOR,
                    RoundingMode.HALF_UP)), shop.getCostsForDay(LocalDate.now()),
                "The actual costs (at the end of the month) are not the same as expected");
        } else {
            assertEquals(BigDecimal.valueOf(10.56).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                shop.getCostsForDay(LocalDate.now()), "The actual costs (not at the end of the month) " +
                    "are not the same as expected");
        }
    }

    @Test
    void testGetCostsForPeriodPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));
        shop.addCashier(cashier);

        BigDecimal totalSalariesAmount = shop.getCashiers().stream()
            .map(Cashier::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        shop.loadNewGoods(newGoodsMap);

        assertEquals(totalSalariesAmount.add(BigDecimal.valueOf(10.56).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
        shop.getCostsForPeriod(LocalDate.now(), LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())),
            "The actual costs for period including the end of the month is not the same as the expected");
    }

    @Test
    void testGetIncomeForDayPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        shop.addIncome(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertEquals(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shop.getIncomeForDay(LocalDate.now()), "The current actual income is not the same as expected");
    }

    @Test
    void testGetIncomeForPeriodPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        shop.addIncome(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertEquals(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shop.getIncomeForPeriod(LocalDate.now(), LocalDate.now().plusDays(6)),
            "The actual income for period is not the same as expected");
    }

    @Test
    void testGetProfitForDayPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));

        shop.addIncome(BigDecimal.valueOf(100).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        // Costs: 10.56
        shop.loadNewGoods(newGoodsMap);

        assertEquals(BigDecimal.valueOf(89.44).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shop.getProfitForDay(LocalDate.now()), "The current actual profit is not the same as expected");
    }

    @Test
    void testGetProfitForPeriodPositiveCase() {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1));
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600));
        shop.addCashier(cashier);

        shop.addIncome(BigDecimal.valueOf(6000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        // Costs: 10.56
        shop.loadNewGoods(newGoodsMap);

        assertEquals(BigDecimal.valueOf(3389.44).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shop.getProfitForPeriod(LocalDate.now(), LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())),
            "The actual profit for period is not the same as expected");
    }
}