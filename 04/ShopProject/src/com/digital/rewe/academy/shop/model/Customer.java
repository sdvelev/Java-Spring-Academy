package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.Shop;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Customer implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private long id;
    private BigDecimal availableMoney;
    private Shop shop;
    private final Map<Good, Double> basket;

    public Customer(long id, BigDecimal availableMoney, Shop shop) {
        this.id = id;
        this.setAvailableMoney(availableMoney);
        this.setShop(shop);
        this.basket = new LinkedHashMap<>();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAvailableMoney() {
        return availableMoney.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public Shop getShop() {
        return shop;
    }

    public Map<Good, Double> getBasket() {
        return this.basket;
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        validateAvailableMoney(availableMoney);
        this.availableMoney = availableMoney;
    }

    public void setShop(Shop shop) {
        validateShop(shop);
        this.shop = shop;
    }

    public void addToBasket(Good good, double amount) {
        validateGood(good);
        validateAmount(amount);
        this.shop.removeGood(good, amount);
        this.basket.putIfAbsent(good, 0.);
        this.basket.merge(good, amount, Double::sum);
    }

    public boolean removeFromBasket(Good good, double amount) {
        validateGood(good);
        validateAmount(amount);
        if (this.basket.containsKey(good) && this.basket.get(good) > amount) {
            this.basket.put(good, this.basket.get(good) - amount);
            this.shop.returnGood(good, amount);
            return true;
        } else if (this.basket.containsKey(good) && this.basket.get(good) == amount) {
            this.basket.remove(good);
            this.shop.returnGood(good, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Objects.equals(shop, customer.shop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shop);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", availableMoney=" + availableMoney.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP) +
            ", basket=" + basket +
            ", shopName=" + shop.getName() +
            '}';
    }

    private void validateAvailableMoney(BigDecimal availableMoney) {
        if (availableMoney == null) {
            throw new NullValueException("The available money of the customer cannot be null");
        }

        if (availableMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The available money of the customer cannot be negative");
        }
    }

    private void validateShop(Shop shop) {
        if (shop == null) {
            throw new NullValueException("The shop argument of the customer cannot be null");
        }
    }

    private void validateGood(Good good) {
        if (good == null) {
            throw new NullValueException("The provided good cannot be null");
        }
    }

    private void validateAmount(double amount) {
        if (amount < 0) {
            throw new NegativeValueException("The amount of the good in basket cannot be negative");
        }
    }
}