package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.validation.CustomerValidator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Customer implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private transient CustomerValidator customerValidator;

    private long id;
    private BigDecimal availableMoney;
    private final Map<Good, Double> basket;

    public Customer(long id, BigDecimal availableMoney, CustomerValidator customerValidator) {
        this.customerValidator = customerValidator;
        this.id = id;
        this.setAvailableMoney(availableMoney);
        this.basket = new LinkedHashMap<>();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAvailableMoney() {
        return availableMoney.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public Map<Good, Double> getBasket() {
        return this.basket;
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        customerValidator.validateAvailableMoney(availableMoney);
        this.availableMoney = availableMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", availableMoney=" + availableMoney +
            ", basket=" + basket +
            '}';
    }
}