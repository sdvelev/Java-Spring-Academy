package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.exception.EndDateBeforeStartDateException;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import com.digital.rewe.academy.shop.validation.GoodValidator;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShopFinancialServiceUnitTest {

    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private Shop shopMock;
    @Mock
    private ShopValidator shopValidatorMock;
    private CashierValidator cashierValidator;
    private CashDeskValidator cashDeskValidator;
    private GoodValidator goodValidator;

    private ShopOperationsService shopOperationsService;
    private ShopFinancialService shopFinancialService;

    @BeforeEach
    void setUp() {
        shopValidatorMock = Mockito.mock(ShopValidator.class);
        cashierValidator = new CashierValidator();
        cashDeskValidator = new CashDeskValidator();
        goodValidator = new GoodValidator();
        shopOperationsService = new ShopOperationsService(shopValidatorMock);
        shopFinancialService = new ShopFinancialService(shopValidatorMock);
        shopMock = Mockito.mock(Shop.class);
    }

    @Test
    void testGetCostsForDayPositiveCaseNoPreviousCosts() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        BigDecimal result = shopFinancialService.getCostsForDay(shopMock, LocalDate.now());

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(BigDecimal.valueOf(7000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result.add(BigDecimal.valueOf(7000)),
                "The actual results of get costs for given day with salaries are not the same as the expected");
            Mockito.verify(shopMock, Mockito.times(1)).getCashiers();
        } else {
            assertEquals(BigDecimal.ZERO.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result, "The actual results of get costs for given day are not the same as the expected");
            Mockito.verify(shopMock, Mockito.never()).getCashiers();
        }
    }

    @Test
    void testGetCostsForDayPositiveCaseWithPreviousCosts() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        costsPerDayMap.put(LocalDate.now(), BigDecimal.valueOf(1000));
        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        BigDecimal result = shopFinancialService.getCostsForDay(shopMock, LocalDate.now());

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(BigDecimal.valueOf(8000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result.add(BigDecimal.valueOf(8000)),
                "The actual results of get costs for given day with salaries are not the same as the expected");
            Mockito.verify(shopMock, Mockito.times(1)).getCashiers();
        } else {
            assertEquals(BigDecimal.valueOf(1000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result, "The actual results of get costs for given day are not the same as the expected");
            Mockito.verify(shopMock, Mockito.never()).getCashiers();
        }
    }

    @Test
    void testGetCostsForPeriodPositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        costsPerDayMap.put(LocalDate.now(), BigDecimal.valueOf(1000));
        costsPerDayMap.put(LocalDate.now().minusDays(1), BigDecimal.valueOf(2000));
        costsPerDayMap.put(LocalDate.now().minusDays(2), BigDecimal.valueOf(3000));
        costsPerDayMap.put(LocalDate.now().minusDays(3), BigDecimal.valueOf(4000));

        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        BigDecimal result = shopFinancialService.getCostsForPeriod(shopMock, LocalDate.now().minusDays(3),
            LocalDate.now());

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(BigDecimal.valueOf(17000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result.add(BigDecimal.valueOf(17000)),
                "The actual results of get costs for given period with salaries are not the same as the expected");
            Mockito.verify(shopMock, Mockito.times(1)).getCashiers();
        } else {
            assertEquals(BigDecimal.valueOf(10000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result, "The actual results of get costs for given period are not the same as the expected");
            Mockito.verify(shopMock, Mockito.never()).getCashiers();
        }
    }

    @Test
    void testGetCostsForPeriodPositiveCornerCase() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();

        costsPerDayMap.put(LocalDate.now().plusDays(3), BigDecimal.valueOf(3000));
        costsPerDayMap.put(LocalDate.now().plusDays(2), BigDecimal.valueOf(2000));
        costsPerDayMap.put(LocalDate.now().plusDays(1), BigDecimal.valueOf(1000));
        costsPerDayMap.put(LocalDate.now(), BigDecimal.valueOf(1000));
        costsPerDayMap.put(LocalDate.now().minusDays(1), BigDecimal.valueOf(2000));
        costsPerDayMap.put(LocalDate.now().minusDays(2), BigDecimal.valueOf(3000));
        costsPerDayMap.put(LocalDate.now().minusDays(3), BigDecimal.valueOf(4000));

        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        BigDecimal result = shopFinancialService.getCostsForPeriod(shopMock, LocalDate.now().minusDays(2),
            LocalDate.now().plusDays(2));

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(BigDecimal.valueOf(16000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result.add(BigDecimal.valueOf(16000)),
                "The actual results of get costs for given period with salaries are not the same as the expected");
            Mockito.verify(shopMock, Mockito.times(1)).getCashiers();
        } else {
            assertEquals(BigDecimal.valueOf(9000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result, "The actual results of get costs for given period are not the same as the expected");
            Mockito.verify(shopMock, Mockito.never()).getCashiers();
        }
    }

    @Test
    void testGetCostsForPeriodEndDateBeforeStartDate() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Mockito.doThrow(EndDateBeforeStartDateException.class).when(shopValidatorMock)
            .validateStartDateBeforeEndDate(Mockito.any(LocalDate.class),  Mockito.any(LocalDate.class));

        assertThrows(EndDateBeforeStartDateException.class, () ->
            shopFinancialService.getCostsForPeriod(shopMock, LocalDate.now().plusDays(2), LocalDate.now()),
            "EndDateBeforeStartDateException is expected but not thrown");

        Mockito.verify(shopMock, Mockito.never()).getCostsPerDayMap();
    }

    @Test
    void testGetIncomeForDayPositiveCaseNoPreviousIncome() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);

        BigDecimal result = shopFinancialService.getIncomeForDay(shopMock, LocalDate.now());

        assertEquals(BigDecimal.ZERO.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            result, "The actual results of get income for given day are not the same as the expected");

        Mockito.verify(shopMock, Mockito.times(1)).getIncomePerDayMap();
    }

    @Test
    void testGetIncomeForDayPositiveCaseWithPreviousIncome() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        incomePerDayMap.put(LocalDate.now(), BigDecimal.valueOf(600));
        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);

        BigDecimal result = shopFinancialService.getIncomeForDay(shopMock, LocalDate.now());

        assertEquals(BigDecimal.valueOf(600).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            result, "The actual results of get income for given day are not the same as the expected");

        Mockito.verify(shopMock, Mockito.times(1)).getIncomePerDayMap();
    }

    @Test
    void testGetIncomeForPeriodPositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        incomePerDayMap.put(LocalDate.now().minusDays(7), BigDecimal.valueOf(100));
        incomePerDayMap.put(LocalDate.now().minusDays(3), BigDecimal.valueOf(200));
        incomePerDayMap.put(LocalDate.now().minusDays(2), BigDecimal.valueOf(300));
        incomePerDayMap.put(LocalDate.now().minusDays(1), BigDecimal.valueOf(400));
        incomePerDayMap.put(LocalDate.now(), BigDecimal.valueOf(600));
        incomePerDayMap.put(LocalDate.now().plusDays(1), BigDecimal.valueOf(700));
        incomePerDayMap.put(LocalDate.now().plusDays(2), BigDecimal.valueOf(800));
        incomePerDayMap.put(LocalDate.now().plusDays(3), BigDecimal.valueOf(900));

        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);

        BigDecimal result = shopFinancialService.getIncomeForPeriod(shopMock, LocalDate.now().minusDays(4),
            LocalDate.now().plusDays(4));

        assertEquals(BigDecimal.valueOf(3900).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            result, "The actual results of get income for given period are not the same as the expected");

        Mockito.verify(shopMock, Mockito.times(9)).getIncomePerDayMap();
    }

    @Test
    void testGetIncomeForPeriodEndDateBeforeStartDate() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Mockito.doThrow(EndDateBeforeStartDateException.class).when(shopValidatorMock)
            .validateStartDateBeforeEndDate(Mockito.any(LocalDate.class),  Mockito.any(LocalDate.class));

        assertThrows(EndDateBeforeStartDateException.class, () ->
                shopFinancialService.getIncomeForPeriod(shopMock, LocalDate.now().plusDays(2), LocalDate.now()),
            "EndDateBeforeStartDateException is expected but not thrown");

        Mockito.verify(shopMock, Mockito.never()).getCostsPerDayMap();
    }

    @Test
    void testGetProfitForDayPositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        costsPerDayMap.put(LocalDate.now(), BigDecimal.valueOf(100));
        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        incomePerDayMap.put(LocalDate.now(), BigDecimal.valueOf(8000));
        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);

        BigDecimal result = shopFinancialService.getProfitForDay(shopMock, LocalDate.now());

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(BigDecimal.valueOf(900).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result.add(BigDecimal.valueOf(900)),
                "The actual results of get profit for given day with salaries are not the same as the expected");
            Mockito.verify(shopMock, Mockito.times(1)).getCashiers();
        } else {
            assertEquals(BigDecimal.valueOf(7900).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result, "The actual results of get profit for given day are not the same as the expected");
            Mockito.verify(shopMock, Mockito.never()).getCashiers();
        }
    }

    @Test
    void testGetProfitForPeriodPositiveCase() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        costsPerDayMap.put(LocalDate.now().minusDays(2), BigDecimal.valueOf(100));
        costsPerDayMap.put(LocalDate.now().minusDays(1), BigDecimal.valueOf(200));
        costsPerDayMap.put(LocalDate.now(), BigDecimal.valueOf(300));

        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        incomePerDayMap.put(LocalDate.now(), BigDecimal.valueOf(600));
        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);

        BigDecimal result = shopFinancialService.getProfitForPeriod(shopMock, LocalDate.now().minusDays(4),
            LocalDate.now());

        if (isEndOfTheMonth(LocalDate.now())) {
            assertEquals(BigDecimal.valueOf(-7000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result.add(BigDecimal.valueOf(-7000)),
                "The actual results of get profit for given period with salaries are not the same as the expected");
            Mockito.verify(shopMock, Mockito.times(1)).getCashiers();
        } else {
            assertEquals(BigDecimal.valueOf(0).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
                result, "The actual results of get profit for given period are not the same as the expected");
            Mockito.verify(shopMock, Mockito.never()).getCashiers();
        }
    }

    @Test
    void testGetProfitForPeriodPositiveCaseSeveralMonths() {
        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> costsPerDayMap = new HashMap<>();
        costsPerDayMap.put(LocalDate.of(2025, 3, 19), BigDecimal.valueOf(100));
        costsPerDayMap.put(LocalDate.of(2025, 3, 18), BigDecimal.valueOf(200));
        costsPerDayMap.put(LocalDate.of(2025, 3, 3), BigDecimal.valueOf(300));

        Mockito.when(shopMock.getCostsPerDayMap()).thenReturn(costsPerDayMap);

        Cashier firstCashier = Mockito.mock(Cashier.class);
        Cashier secondCashier = Mockito.mock(Cashier.class);

        Set<Cashier> cashierSet = new HashSet<>();
        cashierSet.add(firstCashier);
        cashierSet.add(secondCashier);

        Mockito.when(shopMock.getCashiers()).thenReturn(cashierSet);
        Mockito.when(firstCashier.getSalary()).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(secondCashier.getSalary()).thenReturn(BigDecimal.valueOf(4000));

        Mockito.doNothing().when(shopValidatorMock).validateDate(Mockito.any(LocalDate.class));
        Map<LocalDate, BigDecimal> incomePerDayMap = new HashMap<>();
        incomePerDayMap.put(LocalDate.of(2025, 3, 20), BigDecimal.valueOf(600));
        Mockito.when(shopMock.getIncomePerDayMap()).thenReturn(incomePerDayMap);

        BigDecimal result = shopFinancialService.getProfitForPeriod(shopMock, LocalDate.of(2025, 2, 24),
            LocalDate.of(2025, 4, 30));

        assertEquals(BigDecimal.valueOf(-21_000).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), result,
            "The actual results of get profit for given period with salaries are not the same as the expected");
        Mockito.verify(shopMock, Mockito.times(3)).getCashiers();
    }

    @Test
    void testShouldAddSalary() {
        assertTrue(shopFinancialService.shouldAddSalary(LocalDate.of(2025, 3, 31)),
            "The provided date is the end of the month");
        assertTrue(shopFinancialService.shouldAddSalary(LocalDate.of(2030, 1, 31)),
            "The provided date is the end of the month");
        assertTrue(shopFinancialService.shouldAddSalary(LocalDate.of(681, 10, 31)),
            "The provided date is the end of the month");

        assertFalse(shopFinancialService.shouldAddSalary(LocalDate.of(2025, 10, 30)),
            "The provided date is not the end of the month");
        assertFalse(shopFinancialService.shouldAddSalary(LocalDate.of(2025, 3, 19)),
            "The provided date is not the end of the month");
        assertFalse(shopFinancialService.shouldAddSalary(LocalDate.of(2025, 1, 1)),
            "The provided date is not the end of the month");
    }

        private static boolean isEndOfTheMonth(LocalDate date) {
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }
}