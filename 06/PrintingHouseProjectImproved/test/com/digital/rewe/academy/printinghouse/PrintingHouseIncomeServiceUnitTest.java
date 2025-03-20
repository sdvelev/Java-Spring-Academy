package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.order.Order;
import com.digital.rewe.academy.printinghouse.publication.Book;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculator;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculatorAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseIncomeServiceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private PrintingHouseValidator printingHouseValidatorMock;
    private PrintingHouseIncomeServiceAPI printingHouseIncomeService;
    @Mock
    private TariffCalculatorAPI tariffCalculator;
    @Mock
    private PrintingHouse printingHouse;

    @BeforeEach
    void setUp() {
        printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);
        tariffCalculator = Mockito.mock(TariffCalculator.class);
        printingHouseIncomeService = new PrintingHouseIncomeService(printingHouseValidatorMock, tariffCalculator);
        printingHouse = Mockito.mock(PrintingHouse.class);
    }

    @Test
    void testCalculateIncomePositiveCase() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 30);

        Book publicationMock = Mockito.mock(Book.class);
        Mockito.when(publicationMock.getTitle()).thenReturn("Frankenstein; or, The Modern Prometheus");
        Mockito.when(publicationMock.getPublishingDate()).thenReturn(LocalDate.of(1818, 1, 1));
        Mockito.when(publicationMock.getNumberOfPages()).thenReturn(258L);
        Mockito.when(publicationMock.getSizeOfPaper()).thenReturn(PaperSize.A4_SHEET_SIZE);

        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validateDateArguments(Mockito.any(), Mockito.any());

        Mockito.when(tariffCalculator.calculatePrintPrice(Mockito.any(), Mockito.any()))
            .thenReturn(BigDecimal.valueOf(6));

        Set<Order> orderSet = new HashSet<>();
        Order order1 = new Order(1, publicationMock, 6, LocalDate.of(2025, 2, 1));
        Order order2 = new Order(2, publicationMock, 10, LocalDate.of(2025, 3, 1));
        orderSet.add(order1);
        orderSet.add(order2);

        Mockito.when(printingHouse.getOrders()).thenReturn(orderSet);

        BigDecimal expectedExpenses = new BigDecimal("96").setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        BigDecimal actualExpenses = printingHouseIncomeService.calculateIncome(printingHouse, startDate, endDate);
        assertEquals(expectedExpenses, actualExpenses,
            "The actual income for period is not the same as the expected");
    }

    @Test
    void testCalculateIncomeWithNullStartDate() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validateDateArguments(Mockito.any(), Mockito.any());
        assertThrows(NullValueException.class, () -> printingHouseIncomeService.calculateIncome(printingHouse,
            null, LocalDate.now()), "NullValueException is expected but not thrown");
    }
}