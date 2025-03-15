package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class CashDesk {
    private long id;
    private int cashDeskNumber;
    private Optional<Cashier> workingCashier;
    private final Set<Customer> waitingCustomers;

    public CashDesk(long id, int cashDeskNumber) {
        this.id = id;
        this.cashDeskNumber = cashDeskNumber;
        this.workingCashier = Optional.empty();
        this.waitingCustomers = new LinkedHashSet<>();
    }

    public void openCashDesk(Cashier workingCashier) {
        this.workingCashier = Optional.of(workingCashier);
    }

    public void closeCashDesk() {
        this.workingCashier = Optional.empty();
    }

    public boolean isOpen() {
        return this.workingCashier.isPresent();
    }

    public void addCustomerWaiting(Customer customerToAdd) {
        if (isOpen()) {
            this.waitingCustomers.add(customerToAdd);
        } else {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }
}