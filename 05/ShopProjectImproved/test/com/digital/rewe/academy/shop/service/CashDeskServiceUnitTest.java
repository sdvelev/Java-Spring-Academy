package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashDeskServiceUnitTest {
    @Mock
    CashDeskValidator cashDeskValidatorMock;
    @Mock
    CashDesk cashDeskMock;
    @Mock
    private Customer customerMock;

    private CashDeskService cashDeskService;

    @BeforeEach
    void setUp() {
        cashDeskValidatorMock = Mockito.mock(CashDeskValidator.class);
        cashDeskService = new CashDeskService(cashDeskValidatorMock);
        cashDeskMock = Mockito.mock(CashDesk.class);
        customerMock = Mockito.mock(Customer.class);
    }

    @Test
    void testAddCustomerWaitingPositiveCase() {
        Mockito.doNothing().when(cashDeskValidatorMock).validateCustomer(Mockito.any());
        Mockito.when(cashDeskMock.isOpen()).thenReturn(true);

        Set<Customer> waitingCustomers = new LinkedHashSet<>();
        Mockito.when(cashDeskMock.getWaitingCustomers()).thenReturn(waitingCustomers);

        cashDeskService.addCustomerWaiting(cashDeskMock, customerMock);

        assertFalse(waitingCustomers.isEmpty(), "The list of customers waiting is not expected to be empty");
        assertEquals(customerMock, waitingCustomers.iterator().next(),
            "The customer is not the same as the expected");
    }

    @Test
    void testAddCustomerWaitingWithCashDeskClosed() {
        Mockito.doNothing().when(cashDeskValidatorMock).validateCustomer(Mockito.any());
        Mockito.when(cashDeskMock.isOpen()).thenReturn(false);

        assertThrows(CashDeskClosedException.class, () -> cashDeskService.addCustomerWaiting(cashDeskMock, customerMock),
            "Customers cannot be added when cash desk is closed");

        Mockito.verify(cashDeskMock, Mockito.never()).getWaitingCustomers();
    }

    @Test
    void testAddCustomerWaitingWithNullCustomer() {
        Mockito.doThrow(NullValueException.class).when(cashDeskValidatorMock).validateCustomer(Mockito.any());
        assertThrows(NullValueException.class, () -> cashDeskService.addCustomerWaiting(cashDeskMock, null),
            "Customers cannot be added when cash desk is closed");

        Mockito.verify(cashDeskMock, Mockito.never()).isOpen();
    }
}