package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.Shop;
import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CashDeskTest {

    private Shop shopMock = Mockito.mock(Shop.class);
    private Cashier cashierMock = Mockito.mock(Cashier.class);

    @Test
    void testSetCashDeskNumberPositiveCase() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);
        int newCashDeskNumber = 6;
        cashDesk.setCashDeskNumber(newCashDeskNumber);

        assertEquals(newCashDeskNumber, cashDesk.getCashDeskNumber(),
            "Updated value of the number of cash desk is expected but the old value is returned");
    }

    @Test
    void testSetCashDeskNumberWithNegativeValue() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);

        assertThrows(NegativeValueException.class, () -> cashDesk.setCashDeskNumber(-6),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testSetShopWithNullValue() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);

        assertThrows(NullValueException.class, () -> cashDesk.setShop(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testOpenCashDeskPositiveCase() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);
        cashDesk.openCashDesk(cashierMock);

        assertEquals(cashierMock, cashDesk.getWorkingCashier(),
            "The actual working cashier is not the same as the expected");
    }

    @Test
    void testOpenCashDeskWithNullValue() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);

        assertThrows(NullValueException.class, () -> cashDesk.openCashDesk(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testCloseCashDeskWithNullValue() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);
        cashDesk.openCashDesk(cashierMock);
        cashDesk.closeCashDesk();

        assertNull(cashDesk.getWorkingCashier(),
            "Working cashier is expected to be null after closing the cash desk");

    }

    @Test
    void testAddCustomerWaitingCashDeskOpen() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);

        cashDesk.openCashDesk(cashierMock);
        cashDesk.addCustomerWaiting(customer);

        assertEquals(1, cashDesk.getWaitingCustomers().size(),
            "Only one customer is expected to wait at the cash desk");
    }

    @Test
    void testAddCustomerWaitingCashDeskClosed() {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);
        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);

        assertThrows(CashDeskClosedException.class, () -> cashDesk.addCustomerWaiting(customer),
            "Customers cannot be added when the cash desk is closed");
    }
}