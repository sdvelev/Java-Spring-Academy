package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashDeskIntegrationTest {

    private CashDeskValidator cashDeskValidator;
    private CashierValidator cashierValidator;

    @BeforeEach
    void setUp() {
        cashDeskValidator = new CashDeskValidator();
        cashierValidator = new CashierValidator();
    }

    @Test
    void testSetCashDeskNumberPositiveCase() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);
        int newCashDeskNumber = 6;
        cashDesk.setCashDeskNumber(newCashDeskNumber);

        assertEquals(newCashDeskNumber, cashDesk.getCashDeskNumber(),
            "Updated value of the number of cash desk is expected but the old value is returned");
    }

    @Test
    void testSetCashDeskNumberWithNegativeValue() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        assertThrows(NegativeValueException.class, () -> cashDesk.setCashDeskNumber(-6),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void testSetWorkingCashierPositiveCase() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        cashDesk.setWorkingCashier(cashier);

        assertEquals(cashier, cashDesk.getWorkingCashier(),
            "The actual working cashier is not the same as the expected");
    }

    @Test
    void testSetWorkingCashierWithNullValue() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        assertThrows(NullValueException.class, () -> cashDesk.setWorkingCashier(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testCloseCashDeskWithNullValue() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);
        Cashier cashier = new Cashier(1, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        cashDesk.setWorkingCashier(cashier);
        cashDesk.closeCashDesk();

        assertNull(cashDesk.getWorkingCashier(),
            "Working cashier is expected to be null after closing the cash desk");
    }
}