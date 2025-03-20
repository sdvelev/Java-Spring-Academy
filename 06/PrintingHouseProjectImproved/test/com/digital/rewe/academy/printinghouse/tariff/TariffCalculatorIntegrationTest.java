package com.digital.rewe.academy.printinghouse.tariff;

import com.digital.rewe.academy.printinghouse.PrintingHouse;
import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.publication.Book;
import com.digital.rewe.academy.printinghouse.publication.Publication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TariffCalculatorIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private PrintingHouseValidator printingHouseValidator;
    private TariffCalculatorAPI tariffCalculator;

    @BeforeEach
    void setUp() {
        printingHouseValidator = new PrintingHouseValidator();
        tariffCalculator = new TariffCalculator(printingHouseValidator);
    }

    @Test
    void testCalculatePaperExpensesPositiveCaseEvenNumberOfPages() {
        Map<PaperSize, BigDecimal> paperPricePerSize = new EnumMap<>(PaperSize.class);
        paperPricePerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.02));

        PrintingHouse printingHouse = new PrintingHouse(1, "My printing house", null, paperPricePerSize,
            null, printingHouseValidator);

        Publication publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        assertEquals(BigDecimal.valueOf(2.58), tariffCalculator.calculatePaperExpenses(printingHouse, publication),
            "The calculated price for paper expenses is not the same as the expected");
    }

    @Test
    void testCalculatePaperExpensesPositiveCaseOddNumberOfPages() {
        Map<PaperSize, BigDecimal> paperPricePerSize = new EnumMap<>(PaperSize.class);
        paperPricePerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.02));

        PrintingHouse printingHouse = new PrintingHouse(1, "My printing house", null, paperPricePerSize,
            null, printingHouseValidator);

        Publication publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 259,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        assertEquals(BigDecimal.valueOf(2.6).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            tariffCalculator.calculatePaperExpenses(printingHouse, publication),
            "The calculated price for paper expenses is not the same as the expected");
    }

    @Test
    void testCalculatePaperExpensesWithNullPublication() {
        Map<PaperSize, BigDecimal> paperPricePerSize = new EnumMap<>(PaperSize.class);
        paperPricePerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.02));

        PrintingHouse printingHouse = new PrintingHouse(1, "My printing house", null, paperPricePerSize,
            null, printingHouseValidator);

        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePaperExpenses(printingHouse, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testCalculatePaperExpensesWithNullPrintingHouse() {
        Publication publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 259,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePaperExpenses(null, publication),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testCalculatePrintPricePositiveCase() {
        Map<PaperSize, BigDecimal> priceListPerSize = new EnumMap<>(PaperSize.class);
        priceListPerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.35));

        PrintingHouse printingHouse = new PrintingHouse(1, "My printing house", null, null,
            priceListPerSize, printingHouseValidator);

        Publication publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        assertEquals(BigDecimal.valueOf(90.3).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            tariffCalculator.calculatePrintPrice(printingHouse, publication),
            "The calculated price for printing is not the same as the expected");
    }

    @Test
    void testCalculatePrintPriceWithNullPublication() {
        Map<PaperSize, BigDecimal> priceListPerSize = new EnumMap<>(PaperSize.class);
        priceListPerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.35));

        PrintingHouse printingHouse = new PrintingHouse(1, "My printing house", null, null,
            priceListPerSize, printingHouseValidator);

        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePrintPrice(printingHouse, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testCalculatePrintPriceWithNullPrintingHouse() {
        Publication publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);

        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePrintPrice(null, publication),
            "NullValueException is expected but not thrown");
    }
}