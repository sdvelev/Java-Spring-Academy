package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.exception.MarkupPercentagesMissingException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ShopOperationsServiceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private Shop shopMock;
    @Mock
    private ShopValidator shopValidatorMock;

    private ShopOperationsService shopOperationsService;

    @BeforeEach
    void setUp() {
        shopValidatorMock = Mockito.mock(ShopValidator.class);
        shopOperationsService = new ShopOperationsService(shopValidatorMock);
        shopMock = Mockito.mock(Shop.class);
    }

    @Test
    void testGetMarkupPercentagePositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any());
        BigDecimal markupPercentage = BigDecimal.valueOf(0.1);
        Map<GoodCategory, BigDecimal> markupPercentagesMap = new HashMap<>();
        markupPercentagesMap.put(GoodCategory.FOOD, markupPercentage);
        Mockito.when(shopMock.getMarkupPercentages()).thenReturn(markupPercentagesMap);

        assertEquals(markupPercentage,  shopOperationsService.getMarkupPercentage(shopMock, GoodCategory.FOOD),
        "The markup percentage that is returned is not the same as the expected");
    }

    @Test
    void testGetMarkupPercentageMissingException() {
        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any());
        Mockito.when(shopMock.getMarkupPercentages()).thenReturn(new HashMap<>());

        assertThrows(MarkupPercentagesMissingException.class, () ->
                shopOperationsService.getMarkupPercentage(shopMock, GoodCategory.FOOD),
            "MarkupPercentagesMissingException is expected but not thrown");
    }

    @Test
    void testAddMarkupPercentagePositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any());
        Mockito.doNothing().when(shopValidatorMock).validateMarkupPercentage(any());

        GoodCategory newGoodCategory = GoodCategory.NON_FOOD;
        BigDecimal newPercentage = BigDecimal.valueOf(0.2);

        Map<GoodCategory, BigDecimal> markupPercentagesMap = new HashMap<>();
        Mockito.when(shopMock.getMarkupPercentages()).thenReturn(markupPercentagesMap);

        shopOperationsService.addMarkupPercentage(shopMock, newGoodCategory, newPercentage);

        assertTrue(markupPercentagesMap.containsKey(newGoodCategory),
            "The good category is expected to be added but it is not");
        assertEquals(newPercentage, markupPercentagesMap.get(newGoodCategory),
            "The markup percentage that is added is not the same as the expected");
    }

    @Test
    void testAddMarkupPercentageWithGoodCategoryNullValue() {
        Mockito.doThrow(NullValueException.class).when(shopValidatorMock).validateGoodCategory(any());
        assertThrows(NullValueException.class,  () -> shopOperationsService.addMarkupPercentage(shopMock, null,
                BigDecimal.valueOf(0.1)), "NullValueException expected to be thrown when null value is given");

        Mockito.verify(shopValidatorMock, Mockito.never()).validateMarkupPercentage(any());
        Mockito.verify(shopMock, Mockito.never()).getMarkupPercentages();
    }

    @Test
    void testAddMarkupPercentageWithPercentageNegativeValue() {
        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any());
        Mockito.doThrow(NegativeValueException.class).when(shopValidatorMock).validateMarkupPercentage(any());
        assertThrows(NegativeValueException.class,  () -> shopOperationsService.addMarkupPercentage(shopMock, GoodCategory.NON_FOOD,
            BigDecimal.valueOf(-0.1)), "NegativeValueException expected to be thrown when negative value is given");

        Mockito.verify(shopMock, Mockito.never()).getMarkupPercentages();
    }

    @Test
    void testRemoveCashierPositiveCase() {
        Cashier cashierMock = Mockito.mock(Cashier.class);
        Mockito.doNothing().when(shopValidatorMock).validateCashier(any());
        Set<Cashier> cashiersSet = new HashSet<>();
        cashiersSet.add(cashierMock);
        Mockito.when(shopMock.getCashiers()).thenReturn(cashiersSet);
        shopOperationsService.removeCashier(shopMock, cashierMock);
        assertTrue(cashiersSet.isEmpty(), "The cashier collection is expected to be empty but it is not");
    }

    @Test
    void testRemoveCashierUnknownCashier() {
        Cashier cashierMock = Mockito.mock(Cashier.class);
        Mockito.doNothing().when(shopValidatorMock).validateCashier(any());
        Mockito.when(shopMock.getCashiers()).thenReturn(new HashSet<>());

        assertThrows(NoSuchCashierException.class, () -> shopOperationsService.removeCashier(shopMock, cashierMock),
            "NoSuchCashierException is expected but not thrown");
    }

    @Test
    void testRemoveCashDeskPositiveCase() {
        CashDesk cashDeskMock = Mockito.mock(CashDesk.class);
        Mockito.doNothing().when(shopValidatorMock).validateCashDesk(any());
        Set<CashDesk> cashDesksSet = new HashSet<>();
        cashDesksSet.add(cashDeskMock);
        Mockito.when(shopMock.getCashDesks()).thenReturn(cashDesksSet);
        shopOperationsService.removeCashDesk(shopMock, cashDeskMock);
        assertTrue(cashDesksSet.isEmpty(), "The cash desk collection is expected to be empty but it is not");
    }

    @Test
    void testRemoveCashDeskUnknownCashDesk() {
        CashDesk cashDeskMock = Mockito.mock(CashDesk.class);
        Mockito.doNothing().when(shopValidatorMock).validateCashDesk(any());
        Mockito.when(shopMock.getCashDesks()).thenReturn(new HashSet<>());

        assertThrows(NoSuchCashDeskException.class, () ->
                shopOperationsService.removeCashDesk(shopMock, cashDeskMock),
            "NoSuchCashierException is expected but not thrown");
    }

    @Test
    void testLoadNewGoodPositiveCase() {
        Good goodMock = Mockito.mock(Good.class);
        Mockito.when(goodMock.getPurchasePrice()).thenReturn(BigDecimal.TEN);
        Mockito.when(goodMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));
        double quantity = 6;

        Mockito.doNothing().when(shopValidatorMock).validateGood(goodMock);
        Mockito.doNothing().when(shopValidatorMock).validateQuantity(quantity);

        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Map<Good, Double> availableGoodsMap = new HashMap<>();
        Mockito.when(shopMock.getAvailableGoods()).thenReturn(availableGoodsMap);

        shopOperationsService.loadNewGood(shopMock, goodMock, quantity);

        assertTrue(costsPerDayMap.containsKey(LocalDate.now()),
            "The actual key of the costs per day map is not the same as the expected");
        assertEquals(BigDecimal.valueOf(66.).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            costsPerDayMap.get(LocalDate.now()),
            "The actual costs for the day are not the same as the expected");

        assertTrue(availableGoodsMap.containsKey(goodMock),
            "The actual key of the available goods map is not the same as the expected");
        assertEquals(6., availableGoodsMap.get(goodMock),
            "The actual quantity of the available goods is not the same as the expected");
    }

    @Test
    void testAddIncomePositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateAmount(any());
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);
        shopOperationsService.addIncome(shopMock, BigDecimal.valueOf(6000));
        assertTrue(incomePerDayMap.containsKey(LocalDate.now()),
            "The actual key of the income per day map is not the same as the expected");
        assertEquals(BigDecimal.valueOf(6000), incomePerDayMap.get(LocalDate.now()),
            "The actual income for the day is not the same as the expected");
    }

    @Test
    void testReturnGoodPositiveCase() {
        Good goodMock = Mockito.mock(Good.class);
        double quantity = 6;
        Mockito.doNothing().when(shopValidatorMock).validateGood(goodMock);
        Mockito.doNothing().when(shopValidatorMock).validateQuantity(quantity);

        Map<Good, Double> availableGoodsMap = new HashMap<>();
        Mockito.when(shopMock.getAvailableGoods()).thenReturn(availableGoodsMap);

        shopOperationsService.returnGood(shopMock, goodMock, quantity);

        assertTrue(availableGoodsMap.containsKey(goodMock),
            "The actual key of the available goods map is not the same as the expected");
        assertEquals(6., availableGoodsMap.get(goodMock),
            "The actual quantity in the available goods map is not the same as the expected");
    }
}