package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Cashier implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private long id;
    private String name;
    private String shopName;
    private BigDecimal salary;

    public Cashier(long id, String name, String shopName, BigDecimal salary) {
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
        validateName(name);
        this.name = name;
    }

    public void setShopName(String shopName) {
        validateShopName(shopName);
        this.shopName = shopName;
    }

    public void setSalary(BigDecimal salary) {
        validateSalary(salary);
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

    private void validateName(String name) {
        if (name == null) {
            throw new NullValueException("The name of the cashier cannot be null");
        }

        if (name.isBlank()) {
            throw new EmptyValueException("The name of the cashier cannot be empty or blank");
        }
    }

    private void validateShopName(String name) {
        if (name == null) {
            throw new NullValueException("The name of the shop cannot be null");
        }

        if (name.isBlank()) {
            throw new EmptyValueException("The name of the shop cannot be empty or blank");
        }
    }

    private void validateSalary(BigDecimal salary) {
        if (salary == null) {
            throw new NullValueException("The salary of the cashier cannot be null");
        }

        if (salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The salary of the cashier cannot be negative");
        }
    }
}