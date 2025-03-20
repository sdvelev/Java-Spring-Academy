package com.digital.rewe.academy.printinghouse.tariff;

import com.digital.rewe.academy.printinghouse.PrintingHouse;
import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.publication.Publication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TariffCalculatorUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private PrintingHouseValidator printingHouseValidatorMock;
    private TariffCalculatorAPI tariffCalculator;

    @Mock
    Publication publicationMock;
    @Mock
    PrintingHouse printingHouseMock;

    @BeforeEach
    void setUp() {
        printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);
        tariffCalculator = new TariffCalculator(printingHouseValidatorMock);
        printingHouseMock = Mockito.mock(PrintingHouse.class);
        publicationMock = Mockito.mock(Publication.class);
    }

    @Test
    void testCalculatePaperExpensesPositiveCaseEvenNumberOfPages() {
        Map<PaperSize, BigDecimal> paperPricePerSize = new EnumMap<>(PaperSize.class);
        paperPricePerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.02));

        Mockito.when(printingHouseMock.getPaperPricePerSize()).thenReturn(paperPricePerSize);
        Mockito.when(publicationMock.getSizeOfPaper()).thenReturn(PaperSize.A4_SHEET_SIZE);
        Mockito.when(publicationMock.getNumberOfPages()).thenReturn(258L);

        assertEquals(BigDecimal.valueOf(2.58).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            tariffCalculator.calculatePaperExpenses(printingHouseMock, publicationMock),
            "The calculated price for paper expenses is not the same as the expected");
    }

    @Test
    void testCalculatePaperExpensesPositiveCaseOddNumberOfPages() {
        Map<PaperSize, BigDecimal> paperPricePerSize = new EnumMap<>(PaperSize.class);
        paperPricePerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.02));

        Mockito.when(printingHouseMock.getPaperPricePerSize()).thenReturn(paperPricePerSize);
        Mockito.when(publicationMock.getSizeOfPaper()).thenReturn(PaperSize.A4_SHEET_SIZE);
        Mockito.when(publicationMock.getNumberOfPages()).thenReturn(259L);

        assertEquals(BigDecimal.valueOf(2.6).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            tariffCalculator.calculatePaperExpenses(printingHouseMock, publicationMock),
            "The calculated price for paper expenses is not the same as the expected");
    }

    @Test
    void testCalculatePaperExpensesWithNullPublication() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePublication(Mockito.any());
        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePaperExpenses(printingHouseMock, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testCalculatePaperExpensesWithNullPrintingHouse() {
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePaperExpenses(printingHouseMock, null),
            "NullValueException is expected but not thrown");
        Mockito.verify(printingHouseValidatorMock, Mockito.never()).validatePublication(Mockito.any());
    }

    @Test
    void testCalculatePrintPricePositiveCase() {
        Map<PaperSize, BigDecimal> priceListPerSize = new EnumMap<>(PaperSize.class);
        priceListPerSize.put(PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(0.35));

        Mockito.when(printingHouseMock.getPriceListPerSize()).thenReturn(priceListPerSize);
        Mockito.when(publicationMock.getSizeOfPaper()).thenReturn(PaperSize.A4_SHEET_SIZE);
        Mockito.when(publicationMock.getNumberOfPages()).thenReturn(258L);

        assertEquals(BigDecimal.valueOf(90.3).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            tariffCalculator.calculatePrintPrice(printingHouseMock, publicationMock),
            "The calculated price for printing is not the same as the expected");
    }

    @Test
    void testCalculatePrintPriceWithNullPublication() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePublication(Mockito.any());
        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePrintPrice(printingHouseMock, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testCalculatePrintPriceWithNullPrintingHouse() {
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        assertThrows(NullValueException.class, () -> tariffCalculator.calculatePrintPrice(printingHouseMock, null),
            "NullValueException is expected but not thrown");
        Mockito.verify(printingHouseValidatorMock, Mockito.never()).validatePublication(Mockito.any());
    }
}