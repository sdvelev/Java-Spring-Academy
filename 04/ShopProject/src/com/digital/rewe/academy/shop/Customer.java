package com.digital.rewe.academy.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Customer {
    private long id;
    private BigDecimal availableMoney;
    private Map<Good, Double> basket;

    public Customer(long id, BigDecimal availableMoney) {
        this.id = id;
        this.availableMoney = availableMoney;
        this.basket = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAvailableMoney() {
        return availableMoney;
    }

    public Set<Map.Entry<Good, Double>> getBasket() {
        return this.basket.entrySet();
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }

    public void addToBasket(Good good, double amount) {
        //TODO if the shop has enough quantity, then:
        // remove that quantity from the shop
        this.basket.putIfAbsent(good, amount);
    }

    public boolean removeFromBasket(Good good, double amount) {
        if (this.basket.containsKey(good) && this.basket.get(good) > amount) {
            this.basket.put(good, this.basket.get(good) - amount);
            //TODO Add to the shop in:good and in:amount
            return true;
        } else if (this.basket.containsKey(good) && this.basket.get(good) == amount) {
            this.basket.remove(good);
            //TODO Add to the shop in:good and in:amount
            return true;
        }
        return false;
    }



}
