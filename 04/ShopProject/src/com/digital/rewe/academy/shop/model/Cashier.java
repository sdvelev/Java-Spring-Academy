package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Cashier implements Serializable {
    private long id;
    private String name;
    private BigDecimal salary;

    public Cashier(long id, String name, BigDecimal salary) {
        this.id = id;
        this.setName(name);
        this.setSalary(salary);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
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
            ", salary=" + salary +
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

    private void validateSalary(BigDecimal salary) {
        if (salary == null) {
            throw new NullValueException("The salary of the cashier cannot be null");
        }

        if (salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The salary of the cashier cannot be negative");
        }
    }
}