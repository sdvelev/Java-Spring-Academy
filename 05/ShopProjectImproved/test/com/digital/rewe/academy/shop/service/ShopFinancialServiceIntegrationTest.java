package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
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

public class ShopFinancialServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private Shop shop;

    private ShopValidator shopValidator;
    private CashierValidator cashierValidator;
    private GoodValidator goodValidator;

    private ShopFinancialService shopFinancialService;
    private ShopOperationsService shopOperationsService;

    @BeforeEach
    void setUp() {
        shopValidator = new ShopValidator();
        cashierValidator = new CashierValidator();
        goodValidator = new GoodValidator();
        shopFinancialService = new ShopFinancialService(shopValidator);
        shopOperationsService = new ShopOperationsService(shopValidator);
        shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), shopValidator);
    }

    @Test
    void testGetCostsForDayPositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
         shopOperationsService.addCashier(shop, cashier);

        BigDecimal totalSalariesAmount = shop.getCashiers().stream()
            .map(Cashier::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(totalSalariesAmount.add(BigDecimal.valueOf(10.56).setScale(ROUNDING_SCALE_FACTOR,
                    RoundingMode.HALF_UP)), shopFinancialService.getCostsForDay(shop, LocalDate.now()),
                "The actual costs (at the end of the month) are not the same as expected");
        } else {
            assertEquals(BigDecimal.valueOf(10.56).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                shopFinancialService.getCostsForDay(shop, LocalDate.now()), "The actual costs (not at the end of the month) " +
                    "are not the same as expected");
        }
    }

    @Test
    void testGetCostsForPeriodPositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        shopOperationsService.addCashier(shop, cashier);

        BigDecimal totalSalariesAmount = shop.getCashiers().stream()
            .map(Cashier::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        assertEquals(totalSalariesAmount.add(BigDecimal.valueOf(10.56).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            shopFinancialService.getCostsForPeriod(shop, LocalDate.now(), LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())),
            "The actual costs for period including the end of the month is not the same as the expected");
    }

    @Test
    void testGetIncomeForDayPositiveCase() {
        shopOperationsService.addIncome(shop, BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertEquals(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shopFinancialService.getIncomeForDay(shop, LocalDate.now()),
            "The current actual income is not the same as expected");
    }

    @Test
    void testGetIncomeForPeriodPositiveCase() {
        shopOperationsService.addIncome(shop, BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertEquals(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shopFinancialService.getIncomeForPeriod(shop, LocalDate.now(), LocalDate.now().plusDays(6)),
            "The actual income for period is not the same as expected");
    }

    @Test
    void testGetProfitForDayPositiveCase() {
        shopOperationsService.addIncome(shop, BigDecimal.valueOf(100).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        // Costs: 10.56
        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        assertEquals(BigDecimal.valueOf(89.44).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            shopFinancialService.getProfitForDay(shop, LocalDate.now()),
            "The current actual profit is not the same as expected");
    }

    @Test
    void testGetProfitForPeriodPositiveCase() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        shopOperationsService.addCashier(shop, cashier);

        shopOperationsService.addIncome(shop, BigDecimal.valueOf(6000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);

        // Costs: 10.56
        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        assertEquals(BigDecimal.valueOf(3389.44).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
             shopFinancialService.getProfitForPeriod(shop, LocalDate.now(),
                 LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())),
            "The actual profit for period is not the same as expected");
    }

    private static boolean isEndOfTheMonth(LocalDate date) {
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }
}