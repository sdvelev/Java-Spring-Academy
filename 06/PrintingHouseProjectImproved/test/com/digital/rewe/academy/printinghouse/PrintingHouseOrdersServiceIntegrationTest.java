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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrintingHouseOrdersServiceIntegrationTest {

    private static final int ROUNDING_SCALE_FACTOR = 2;

    private PrintingHouseValidator printingHouseValidator;
    private TariffCalculatorAPI tariffCalculator;
    private PrintingHouseOrdersServiceAPI printingHouseOrdersService;
    private PrintingHouseOperationsServiceAPI printingHouseOperationsService;
    private PrintingHouse printingHouse;

    @BeforeEach
    void setUp() {
        printingHouseValidator = new PrintingHouseValidator();
        tariffCalculator = new TariffCalculator(printingHouseValidator);
        printingHouseOrdersService = new PrintingHouseOrdersService(printingHouseValidator, tariffCalculator);
        printingHouseOperationsService = new PrintingHouseOperationsService(printingHouseValidator);
        printingHouse = new PrintingHouse(1, "My printing house", printingHouseValidator);
    }

    @Test
    void testMakeOrderPositiveCase() {
        Book publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        printingHouseOperationsService.addPrintPrice(printingHouse, PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.35));

        BigDecimal expectedExpenses = new BigDecimal("903").setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        BigDecimal actualExpenses = printingHouseOrdersService.makeOrder(printingHouse, publication, 10);
        assertEquals(expectedExpenses, actualExpenses,
            "The actual income for the order is not the same as the expected");
        assertFalse(printingHouse.getOrders().isEmpty(),
            "The list of order is not expected to be empty");
        assertTrue(printingHouse.getOrders().iterator().next().publication().equals(publication),
            "The expected publication in the order is not the same as the actual");
    }

    @Test
    void testCalculateIncomeWithNullPublication() {
        assertThrows(NullValueException.class, () -> printingHouseOrdersService.makeOrder(printingHouse,
            null, 10), "NullValueException is expected but not thrown");
    }
}