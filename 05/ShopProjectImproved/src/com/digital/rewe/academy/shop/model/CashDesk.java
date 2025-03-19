package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.validation.CashDeskValidator;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class CashDesk implements Serializable {

    private transient CashDeskValidator cashDeskValidator;

    private long id;
    private int cashDeskNumber;
    private Cashier workingCashier;
    private final Set<Customer> waitingCustomers;

    public CashDesk(long id, int cashDeskNumber, CashDeskValidator cashDeskValidator) {
        this.cashDeskValidator = cashDeskValidator;
        this.id = id;
        this.setCashDeskNumber(cashDeskNumber);
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

    public boolean isOpen() {
        return workingCashier != null;
    }

    public void closeCashDesk() {
        this.workingCashier = null;
    }

    public void setCashDeskNumber(int cashDeskNumber) {
        cashDeskValidator.validateCashDeskNumber(cashDeskNumber);
        this.cashDeskNumber = cashDeskNumber;
    }

    public void setWorkingCashier(Cashier workingCashier) {
        cashDeskValidator.validateCashier(workingCashier);
        this.workingCashier = workingCashier;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CashDesk cashDesk = (CashDesk) o;
        return id == cashDesk.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CashDesk{" +
            "id=" + id +
            ", cashDeskNumber=" + cashDeskNumber +
            ", workingCashier=" + workingCashier +
            ", waitingCustomers=" + waitingCustomers +
            '}';
    }
}