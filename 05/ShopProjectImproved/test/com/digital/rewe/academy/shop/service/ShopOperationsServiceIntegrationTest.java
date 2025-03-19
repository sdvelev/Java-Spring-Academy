package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;
import com.digital.rewe.academy.shop.exception.NoSuchGoodException;
import com.digital.rewe.academy.shop.exception.NoSuchQuantityException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import com.digital.rewe.academy.shop.validation.GoodValidator;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShopOperationsServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private Shop shop;
    private ShopValidator shopValidator;
    private CashierValidator cashierValidator;
    private CashDeskValidator cashDeskValidator;
    private GoodValidator goodValidator;

    private ShopOperationsService shopOperationsService;
    private ShopFinancialService shopFinancialService;

    @BeforeEach
    void setUp() {
        shopValidator = new ShopValidator();
        cashierValidator = new CashierValidator();
        cashDeskValidator = new CashDeskValidator();
        goodValidator = new GoodValidator();
        shopOperationsService = new ShopOperationsService(shopValidator);
        shopFinancialService = new ShopFinancialService(shopValidator);
        shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), shopValidator);
    }

    @Test
    void testAddMarkupPercentagePositiveCase() {
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.FOOD, BigDecimal.valueOf(0.1));

        assertEquals(BigDecimal.valueOf(0.1), shopOperationsService.getMarkupPercentage(shop, GoodCategory.FOOD),
            "Actual markup percentage is not the same as the expected");
    }

    @Test
    void testAddMarkupPercentageWithGoodCategoryNullValue() {
        assertThrows(NullValueException.class,  () -> shopOperationsService.addMarkupPercentage(shop, null, BigDecimal.valueOf(0.1)),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testAddMarkupPercentageWithPercentageNegativeValue() {
        assertThrows(NegativeValueException.class,  () -> shopOperationsService.addMarkupPercentage(shop,
                GoodCategory.NON_FOOD, BigDecimal.valueOf(-0.1)),
            "NegativeValueException expected to be thrown when negative value for markup percentage is given");
    }

    @Test
    void testRemoveCashierPositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        shopOperationsService.addCashier(shop, cashier);
        shopOperationsService.removeCashier(shop, cashier);

        assertTrue(shop.getCashiers().isEmpty(), "The cashier collection is expected to be empty but it is not");
    }

    @Test
    void testRemoveCashierUnknownCashier() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        assertThrows(NoSuchCashierException.class, () -> shopOperationsService.removeCashier(shop, cashier),
            "NoSuchCashierException is expected but not thrown");
    }

    @Test
    void testRemoveCashDeskPositiveCase() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        shopOperationsService.addCashDesk(shop, cashDesk);
        shopOperationsService.removeCashDesk(shop, cashDesk);

        assertTrue(shop.getCashDesks().isEmpty(), "The cash desk collection is expected to be empty but it is not");
    }

    @Test
    void testRemoveCashDeskUnknownCashDesk() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        assertThrows(NoSuchCashDeskException.class, () -> shopOperationsService.removeCashDesk(shop, cashDesk),
            "NoSuchCashDeskException is expected but not thrown");
    }

    @Test
    void testLoadNewGoodsPositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        newGoodsMap.put(book, 1.);

        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        assertEquals(BigDecimal.valueOf(32.45).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shopFinancialService.getCostsForDay(shop, LocalDate.now()), "The current daily costs are not the same as the expected");
        assertTrue(shop.getAvailableGoods().containsKey(water) && shop.getAvailableGoods().get(water).equals(6.),
            "Part of the expected goods with quantities are missing");
        assertTrue(shop.getAvailableGoods().containsKey(book) && shop.getAvailableGoods().get(book).equals(1.),
            "Part of the expected goods with quantities are missing");
    }

    @Test
    void testAddIncomePositiveCase() {
        shopOperationsService.addIncome(shop, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shopFinancialService.getIncomeForDay(shop, LocalDate.now()),
            "The current daily income is not the same as the expected");
    }

    @Test
    void testRemoveGoodPositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        newGoodsMap.put(book, 1.);

        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        shopOperationsService.removeGood(shop, water, 2);
        shopOperationsService.removeGood(shop, book, 1);

        assertTrue(shop.getAvailableGoods().containsKey(water) && shop.getAvailableGoods().get(water).equals(4.),
            "Part of the expected goods with quantities are missing or incorrect number");
        assertFalse(shop.getAvailableGoods().containsKey(book),
            "Part of the expected goods with quantities are present but they are expected to miss");
    }

    @Test
    void testRemoveGoodNotEnoughQuantity() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        assertThrows(NoSuchQuantityException.class, () -> shopOperationsService.removeGood(shop, water, 10),
            "NoSuchQuantityException is expected but not thrown");
    }

    @Test
    void testRemoveGoodNoSuchGood() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        assertThrows(NoSuchGoodException.class, () -> shopOperationsService.removeGood(shop, book, 1),
            "NoSuchGoodException is expected but not thrown");
    }

    @Test
    void testReturnGoodPositiveCase() {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        shopOperationsService.removeGood(shop, water, 2.);
        shopOperationsService.returnGood(shop, water, 1.);

        assertTrue(shop.getAvailableGoods().containsKey(water) && shop.getAvailableGoods().get(water).equals(5.),
            "Part of the expected goods with quantities are missing or incorrect number");
    }
}