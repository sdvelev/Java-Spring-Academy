package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.CashDeskServiceAPI;
import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;


public class CashDeskService implements CashDeskServiceAPI {

    private CashDeskValidator cashDeskValidator;

    public CashDeskService(CashDeskValidator cashDeskValidator) {
        this.cashDeskValidator = cashDeskValidator;
    }

    @Override
    public void addCustomerWaiting(CashDesk cashDesk, Customer customerToAdd) {
        cashDeskValidator.validateCashDesk(cashDesk);
        cashDeskValidator.validateCustomer(customerToAdd);
        if (cashDesk.isOpen()) {
            cashDesk.getWaitingCustomers().add(customerToAdd);
        } else {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }
}