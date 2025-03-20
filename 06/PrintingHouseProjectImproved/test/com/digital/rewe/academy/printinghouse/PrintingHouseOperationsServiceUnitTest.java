package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseOperationsServiceUnitTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private PrintingHouseValidator printingHouseValidatorMock;
    private PrintingHouseOperationsServiceAPI printingHouseOperationsService;
    @Mock
    private PrintingHouse printingHouseMock;

    @BeforeEach
    void setUp() {
        printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);
        printingHouseMock = Mockito.mock(PrintingHouse.class);
        printingHouseOperationsService = new PrintingHouseOperationsService(printingHouseValidatorMock);
    }

    @Test
    void testAddPaperPricePositiveCase() {
       Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
       Mockito.doNothing().when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
       Mockito.doNothing().when(printingHouseValidatorMock).validatePrice(Mockito.any());

       Map<PaperSize, BigDecimal> paperPricePerSizeMap = new HashMap<>();
       Mockito.when(printingHouseMock.getPaperPricePerSize()).thenReturn(paperPricePerSizeMap);

        printingHouseOperationsService.addPaperPrice(printingHouseMock, PaperSize.A4_SHEET_SIZE,
            BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertFalse(paperPricePerSizeMap.isEmpty(),
            "The list of sheet prices is not expected to be empty but it is");
        assertEquals(BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            paperPricePerSizeMap.get(PaperSize.A4_SHEET_SIZE),
            "The list of sheet prices is expected to have specific key");
    }

    @Test
    void testAddPaperPriceWithNullPaperSize() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        assertThrows(NullValueException.class, () -> printingHouseOperationsService.addPaperPrice(printingHouseMock,
                null, BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NullValueException is expected but not thrown");
        Mockito.verify(printingHouseValidatorMock, Mockito.never()).validatePrice(Mockito.any());
        Mockito.verify(printingHouseMock, Mockito.never()).getPaperPricePerSize();
    }

    @Test
    void testAddPaperPriceWithNegativeSheetPrice() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        Mockito.doThrow(NegativeValueException.class).when(printingHouseValidatorMock).validatePrice(Mockito.any());
        assertThrows(NegativeValueException.class, () -> printingHouseOperationsService.addPaperPrice(printingHouseMock,
                PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(-0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NegativeValueException is expected but not thrown");
        Mockito.verify(printingHouseMock, Mockito.never()).getPaperPricePerSize();
    }

    @Test
    void testAddPrintPricePositiveCase() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrice(Mockito.any());

        Map<PaperSize, BigDecimal> priceListPerSizeMap = new HashMap<>();
        Mockito.when(printingHouseMock.getPriceListPerSize()).thenReturn(priceListPerSizeMap);

        printingHouseOperationsService.addPrintPrice(printingHouseMock, PaperSize.A4_SHEET_SIZE,
            BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP));

        assertFalse(priceListPerSizeMap.isEmpty(),
            "The list of sheet prices is not expected to be empty but it is");
        assertEquals(BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            priceListPerSizeMap.get(PaperSize.A4_SHEET_SIZE),
            "The list of sheet prices is expected to have specific key");
    }

    @Test
    void testAddPrintPriceWithNullPaperSize() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        assertThrows(NullValueException.class, () -> printingHouseOperationsService.addPrintPrice(printingHouseMock,
                null, BigDecimal.valueOf(0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NullValueException is expected but not thrown");
        Mockito.verify(printingHouseValidatorMock, Mockito.never()).validatePrice(Mockito.any());
        Mockito.verify(printingHouseMock, Mockito.never()).getPaperPricePerSize();
    }

    @Test
    void testAddPrintPriceWithNegativeSheetPrice() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        Mockito.doThrow(NegativeValueException.class).when(printingHouseValidatorMock).validatePrice(Mockito.any());
        assertThrows(NegativeValueException.class, () -> printingHouseOperationsService.addPrintPrice(printingHouseMock,
                PaperSize.A4_SHEET_SIZE, BigDecimal.valueOf(-0.02).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP)),
            "NegativeValueException is expected but not thrown");
        Mockito.verify(printingHouseMock, Mockito.never()).getPaperPricePerSize();
    }
}