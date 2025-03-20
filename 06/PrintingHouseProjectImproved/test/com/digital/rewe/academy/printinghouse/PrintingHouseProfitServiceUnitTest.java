package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseProfitServiceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private PrintingHouseValidator printingHouseValidatorMock;
    @Mock
    private PrintingHouseIncomeServiceAPI printingHouseIncomeServiceMock;
    @Mock
    private PrintingHouseExpensesServiceAPI printingHouseExpensesServiceMock;
    private PrintingHouseProfitServiceAPI printingHouseProfitService;
    @Mock
    private PrintingHouse printingHouseMock;

    @BeforeEach
    void setUp() {
        printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);
        printingHouseIncomeServiceMock = Mockito.mock(PrintingHouseIncomeService.class);
        printingHouseExpensesServiceMock = Mockito.mock(PrintingHouseExpensesService.class);
        printingHouseProfitService = new PrintingHouseProfitService(printingHouseValidatorMock, printingHouseExpensesServiceMock,
            printingHouseIncomeServiceMock);
        printingHouseMock = Mockito.mock(PrintingHouse.class);
    }

    @Test
    void testCalculateProfitPositiveCase() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validateDateArguments(Mockito.any(), Mockito.any());

        Mockito.when(printingHouseIncomeServiceMock.calculateIncome(Mockito.any(), Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class))).thenReturn(BigDecimal.valueOf(100));

        Mockito.when(printingHouseExpensesServiceMock.calculateExpenses(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(BigDecimal.valueOf(20));

        BigDecimal expectedExpenses = new BigDecimal("80").setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        BigDecimal actualExpenses = printingHouseProfitService.calculateProfit(printingHouseMock, LocalDate.now(), LocalDate.now());
        assertEquals(expectedExpenses, actualExpenses,
            "The actual profit for period is not the same as the expected");
    }

    @Test
    void testCalculateProfitWithNullStartDate() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validateDateArguments(Mockito.any(), Mockito.any());
        assertThrows(NullValueException.class, () -> printingHouseProfitService.calculateProfit(printingHouseMock,
            null, LocalDate.now()), "NullValueException is expected but not thrown");
    }
}