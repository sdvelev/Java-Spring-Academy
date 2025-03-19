package com.digital.rewe.academy.shop.validation;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NoCustomerWaitingException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Shop;

public class CashDeskValidator {
    public void validateCashDeskNumber(int cashDeskNumber) {
        if (cashDeskNumber < 0) {
            throw new NegativeValueException("The number of the cash desk cannot be negative");
        }
    }

    public void validateCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullValueException("The cashier of the cash desk cannot be null");
        }
    }

    public void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new NullValueException("The customer argument cannot be null");
        }
    }

    public void validateCashDesk(CashDesk cashDesk) {
        if (cashDesk == null) {
            throw new NullValueException("The provided cash desk cannot be null");
        }
    }

    public void validateCashDescOpen(CashDesk cashDesk) {
        if (cashDesk.getWorkingCashier() == null) {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }

    public void validateCustomersWaiting(CashDesk cashDesk) {
        if (cashDesk.getWaitingCustomers().isEmpty()) {
            throw new NoCustomerWaitingException("There is not a customer waiting to be served");
        }
    }

    public void validateShop(Shop shop) {
        if (shop == null) {
            throw new NullValueException("The shop argument cannot be null");
        }
    }
}