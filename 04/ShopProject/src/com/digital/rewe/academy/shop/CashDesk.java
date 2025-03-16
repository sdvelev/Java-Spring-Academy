package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NullValueException;


import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class CashDesk implements Serializable {
    private long id;
    private int cashDeskNumber;
    private Shop shop;
    private Cashier workingCashier;
    private final Set<Customer> waitingCustomers;

    public CashDesk(long id, int cashDeskNumber, Shop shop) {
        this.id = id;
        this.cashDeskNumber = cashDeskNumber;
        this.shop = shop;
        this.workingCashier = null;
        this.waitingCustomers = new LinkedHashSet<>();
    }

    public long getId() {
        return id;
    }

    public int getCashDeskNumber() {
        return cashDeskNumber;
    }

    public Cashier getWorkingCashier() {
        return workingCashier;
    }

    public Set<Customer> getWaitingCustomers() {
        return waitingCustomers;
    }

    public Shop getShop() {
        return shop;
    }

    public void setCashDeskNumber(int cashDeskNumber) {
        this.cashDeskNumber = cashDeskNumber;
    }

    public void openCashDesk(Cashier workingCashier) {
        validateCashierIsNull(workingCashier);
        this.workingCashier = workingCashier;
    }

    public void closeCashDesk() {
        this.workingCashier = null;
    }

    public boolean isOpen() {
        return this.workingCashier != null;
    }

    public void addCustomerWaiting(Customer customerToAdd) {
        if (isOpen()) {
            validateCustomerIsNull(customerToAdd);
            this.waitingCustomers.add(customerToAdd);
        } else {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }

    private void validateCashierIsNull(Cashier cashier) {
        if (cashier == null) {
            throw new NullValueException("The cashier argument cannot be null");
        }
    }

    private void validateCustomerIsNull(Customer customer) {
        if (customer == null) {
            throw new NullValueException("The customer argument cannot be null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CashDesk cashDesk = (CashDesk) o;
        return id == cashDesk.id && Objects.equals(shop, cashDesk.shop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shop);
    }

    @Override
    public String toString() {
        return "CashDesk{" +
            "id=" + id +
            ", cashDeskNumber=" + cashDeskNumber +
            ", workingCashier=" + workingCashier +
            '}';
    }
}