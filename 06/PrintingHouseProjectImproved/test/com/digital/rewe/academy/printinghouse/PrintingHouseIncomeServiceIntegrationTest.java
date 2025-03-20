package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.publication.Book;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculator;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculatorAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseIncomeServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private PrintingHouseValidator printingHouseValidator;
    private PrintingHouseIncomeServiceAPI printingHouseIncomeService;
    private TariffCalculatorAPI tariffCalculator;
    private PrintingHouseOrdersServiceAPI printingHouseOrdersService;
    private PrintingHouseOperationsServiceAPI printingHouseOperationsService;
    private PrintingHouse printingHouse;

    @BeforeEach
    void setUp() {
        printingHouseValidator = new PrintingHouseValidator();
        tariffCalculator = new TariffCalculator(printingHouseValidator);
        printingHouseIncomeService = new PrintingHouseIncomeService(printingHouseValidator, tariffCalculator);
        printingHouseOrdersService = new PrintingHouseOrdersService(printingHouseValidator, tariffCalculator);
        printingHouseOperationsService = new PrintingHouseOperationsService(printingHouseValidator);
        printingHouse = new PrintingHouse(1, "My printing house", printingHouseValidator);
    }

    @Test
    void testCalculateIncomePositiveCase() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 30);

        Book publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        printingHouseOperationsService.addPrintPrice(printingHouse, PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.35));
        printingHouseOperationsService.addPaperPrice(printingHouse, PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.02));

        printingHouseOrdersService.makeOrder(printingHouse, publication, 6);
        printingHouseOrdersService.makeOrder(printingHouse, publication, 10);

        BigDecimal expectedExpenses = new BigDecimal("1444.8").setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        BigDecimal actualExpenses = printingHouseIncomeService.calculateIncome(printingHouse, startDate, endDate);
        assertEquals(expectedExpenses, actualExpenses,
            "The actual income for period are not the same as the expected");
    }

    @Test
    void testCalculateIncomeWithNullEndDate() {
        assertThrows(NullValueException.class, () -> printingHouseIncomeService.calculateIncome(printingHouse,
            LocalDate.now(), null), "NullValueException is expected but not thrown");
    }
}