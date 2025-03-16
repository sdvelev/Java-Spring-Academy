package com.digital.rewe.academy.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Customer implements Serializable {
    private long id;
    private BigDecimal availableMoney;
    private final Map<Good, Double> basket;
    private Shop shop;

    public Customer(long id, BigDecimal availableMoney, Shop shop) {
        this.id = id;
        this.availableMoney = availableMoney;
        this.shop = shop;
        this.basket = new LinkedHashMap<>();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAvailableMoney() {
        return availableMoney;
    }

    public Map<Good, Double> getBasket() {
        return this.basket;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }

    public void addToBasket(Good good, double amount) {
        this.shop.removeGood(good, amount);
        this.basket.merge(good, amount, Double::sum);
    }

    public boolean removeFromBasket(Good good, double amount) {
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
            ", availableMoney=" + availableMoney +
            ", basket=" + basket +
            ", shop=" + shop +
            '}';
    }
}