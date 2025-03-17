package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.Shop;
import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
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
        this.setCashDeskNumber(cashDeskNumber);
        this.setShop(shop);
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

    public boolean isOpen() {
        return this.workingCashier != null;
    }

    public void setCashDeskNumber(int cashDeskNumber) {
        validateCashDeskNumber(cashDeskNumber);
        this.cashDeskNumber = cashDeskNumber;
    }

    public void setShop(Shop shop) {
        validateShop(shop);
        this.shop = shop;
    }

    public void openCashDesk(Cashier workingCashier) {
        validateCashier(workingCashier);
        this.workingCashier = workingCashier;
    }

    public void closeCashDesk() {
        this.workingCashier = null;
    }

    public void addCustomerWaiting(Customer customerToAdd) {
        validateCustomer(customerToAdd);
        if (isOpen()) {
            this.waitingCustomers.add(customerToAdd);
        } else {
            throw new CashDeskClosedException("The current cash desk is closed");
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
            ", shop=" + shop +
            ", workingCashier=" + workingCashier +
            ", waitingCustomers=" + waitingCustomers +
            '}';
    }

    private void validateCashDeskNumber(int cashDeskNumber) {
        if (cashDeskNumber < 0) {
            throw new NegativeValueException("The number of the cash desk cannot be negative");
        }
    }

    private void validateShop(Shop shop) {
        if (shop == null) {
            throw new NullValueException("The shop field cannot be null");
        }
    }

    private void validateCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullValueException("The cashier of the cash desk cannot be null");
        }
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new NullValueException("The customer argument cannot be null");
        }
    }
}