package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrintingHouseOperationsServiceIntegrationTest {

    private static final int ROUNDING_SCALE_FACTOR = 2;

    private PrintingHouseValidator printingHouseValidator;
    private PrintingHouseOperationsServiceAPI printingHouseOperationsService;
    private PrintingHouse printingHouse;

    @BeforeEach
    void setUp() {
        printingHouseValidator = new PrintingHouseValidator();
        printingHouseOperationsService = new PrintingHouseOperationsService(printingHouseValidator);
        printingHouse = new PrintingHouse(1, "My printing house", printingHouseValidator);
    }

    @Test
    void testAddPaperPricePositiveCase() {
        assertTrue(printingHouse.getPaperPricePerSize().isEmpty(),
            "The list of sheet prices is expected to be empty but it is not");

        printingHouseOperationsService.addPaperPrice(printingHouse, PaperSize.A4_SHEET_SIZE,
            BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertFalse(printingHouse.getPaperPricePerSize().isEmpty(),
            "he list of sheet prices is not expected to be empty but it is");
        assertEquals(BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            printingHouse.getPaperPricePerSize().get(PaperSize.A4_SHEET_SIZE),
            "The list of sheet prices is expected to have specific key");
    }

    @Test
    void testAddPaperPriceWithNullPaperSize() {
        assertThrows(NullValueException.class, () -> printingHouseOperationsService.addPaperPrice(printingHouse,
                null, BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testAddPaperPriceWithNegativeSheetPrice() {
        assertThrows(NegativeValueException.class, () -> printingHouseOperationsService.addPaperPrice(printingHouse,
                PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(-0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NegativeValueException is expected but not thrown");
    }

    @Test
    void testAddPrintPricePositiveCase() {
        assertTrue(printingHouse.getPriceListPerSize().isEmpty(),
            "The list of print prices is expected to be empty but it is not");

        printingHouseOperationsService.addPrintPrice(printingHouse, PaperSize.A4_SHEET_SIZE,
            BigDecimal.valueOf(0.35).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertFalse(printingHouse.getPriceListPerSize().isEmpty(),
            "he list of print prices is not expected to be empty but it is");
        assertEquals(BigDecimal.valueOf(0.35).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            printingHouse.getPriceListPerSize().get(PaperSize.A4_SHEET_SIZE),
            "The list of print prices is expected to have specific key");
    }

    @Test
    void testAddPrintPriceWithNullPaperSize() {
        assertThrows(NullValueException.class, () -> printingHouseOperationsService.addPrintPrice(printingHouse,
                null, BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testAddPrintPriceWithNegativeSheetPrice() {
        assertThrows(NegativeValueException.class, () -> printingHouseOperationsService.addPrintPrice(printingHouse,
                PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(-0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NegativeValueException is expected but not thrown");
    }
}