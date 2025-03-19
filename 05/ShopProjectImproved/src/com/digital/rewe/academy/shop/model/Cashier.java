package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.validation.CashierValidator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Cashier implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private transient CashierValidator cashierValidator;

    private long id;
    private String name;
    private String shopName;
    private BigDecimal salary;

    public Cashier(long id, String name, String shopName, BigDecimal salary, CashierValidator cashierValidator) {
        this.cashierValidator = cashierValidator;
        this.id = id;
        this.setName(name);
        this.setShopName(shopName);
        this.setSalary(salary);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShopName() {
        return shopName;
    }

    public BigDecimal getSalary() {
        return salary.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public void setName(String name) {
        cashierValidator.validateName(name);
        this.name = name;
    }

    public void setShopName(String shopName) {
        cashierValidator.validateShopName(shopName);
        this.shopName = shopName;
    }

    public void setSalary(BigDecimal salary) {
        cashierValidator.validateSalary(salary);
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return id == cashier.id && Objects.equals(name, cashier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Cashier{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", shopName='" + shopName + '\'' +
            ", salary=" + salary.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP) +
            '}';
    }
}