package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.order.Order;
import com.digital.rewe.academy.printinghouse.publication.Publication;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseOrdersServiceUnitTest {

    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private PrintingHouseValidator printingHouseValidatorMock;
    @Mock
    private TariffCalculatorAPI tariffCalculatorMock;
    private PrintingHouseOrdersServiceAPI printingHouseOrdersService;
    @Mock
    private PrintingHouse printingHouseMock;

    @BeforeEach
    void setUp() {
        printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);
        tariffCalculatorMock = Mockito.mock(TariffCalculator.class);
        printingHouseOrdersService = new PrintingHouseOrdersService(printingHouseValidatorMock, tariffCalculatorMock);
        printingHouseMock = Mockito.mock(PrintingHouse.class);
    }

    @Test
    void testMakeOrderPositiveCase() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validatePublication(Mockito.any());
        Mockito.doNothing().when(printingHouseValidatorMock).validateNumberOfCopies(Mockito.anyLong());

        Set<Order> orderSet = new HashSet<>();
        Mockito.when(printingHouseMock.getOrders()).thenReturn(orderSet);

        Publication publicationMock = Mockito.mock(Publication.class);

        Mockito.when(printingHouseMock.getCounterOfOrderInstances()).thenReturn(1L);

        Order order = new Order(1, publicationMock, 6, LocalDate.now());

        Mockito.when(tariffCalculatorMock.calculatePrintPrice(Mockito.any(), Mockito.any()))
            .thenReturn(new BigDecimal("1"));

        BigDecimal actualIncome = printingHouseOrdersService.makeOrder(printingHouseMock, publicationMock, 6);

        BigDecimal expectedIncome = new BigDecimal("6").setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
        assertEquals(expectedIncome, actualIncome,
            "The actual income for the order is not the same as the expected");
        assertFalse(orderSet.isEmpty(),
            "The list of order is not expected to be empty");
        assertEquals(order, orderSet.iterator().next(),
            "The expected order after making order is not the same as the actual");
    }

    @Test
    void testMakeOrderWithNullPublication() {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePrintingHouse(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePublication(Mockito.any());
        assertThrows(NullValueException.class, () -> printingHouseOrdersService.makeOrder(printingHouseMock,
            null, 10), "NullValueException is expected but not thrown");
        Mockito.verify(printingHouseMock, Mockito.never()).getOrders();
    }
}