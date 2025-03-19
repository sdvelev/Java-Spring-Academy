package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import com.digital.rewe.academy.shop.validation.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashDeskServiceIntegrationTest {

    private CashDeskValidator cashDeskValidator;
    private CustomerValidator customerValidator;
    private CashierValidator cashierValidator;
    private CashDeskService cashDeskService;

    private CashDesk cashDesk;
    private Customer customer;

    @BeforeEach
    void setUp() {
        cashDeskValidator = new CashDeskValidator();
        customerValidator = new CustomerValidator();
        cashierValidator = new CashierValidator();
        cashDeskService = new CashDeskService(cashDeskValidator);
        cashDesk = new CashDesk(1, 5, cashDeskValidator);
        customer = new Customer(1, BigDecimal.valueOf(100), customerValidator);

    }

    @Test
    void testAddCustomerWaitingCashDeskOpen() {
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        cashDesk.setWorkingCashier(cashier);
        cashDeskService.addCustomerWaiting(cashDesk, customer);

        assertEquals(1, cashDesk.getWaitingCustomers().size(),
            "Only one customer is expected to wait at the cash desk");
    }

    @Test
    void testAddCustomerWaitingCashDeskClosed() {
        assertThrows(CashDeskClosedException.class, () -> cashDeskService.addCustomerWaiting(cashDesk, customer),
            "Customers cannot be added when the cash desk is closed");
    }

    @Test
    void testAddCustomerWaitingWithNullCustomer() {
        assertThrows(NullValueException.class, () -> cashDeskService.addCustomerWaiting(cashDesk, null),
            "NullValueException is expected but not thrown");
    }
}