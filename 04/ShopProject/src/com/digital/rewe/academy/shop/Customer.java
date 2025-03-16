package com.digital.rewe.academy.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Customer {
    private long id;
    private BigDecimal availableMoney;
    private Map<Good, Double> basket;
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

    public void setAvailableMoney(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }

    public void addToBasket(Good good, double amount) {
        //TODO if the shop has enough quantity, then:
        // remove that quantity from the shop
        shop.removeGood(good, amount);
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

//    public boolean buy(Shop shop, Good good, double amount) {
//        if (shop == null || good == null) {
//            throw new IllegalArgumentException("Shop and Good cannot be null");
//        }
//
//        // Check if the shop has enough quantity of the good
//        Double availableQuantity = shop.getAvailableGoods().get(good);
//        if (availableQuantity == null || availableQuantity < amount) {
//            return false;
//        }
//
//        // Calculate the total price
//        BigDecimal totalPrice = good.getSellingPrice().multiply(BigDecimal.valueOf(amount));
//
//        // Check if the customer has enough money
//        if (this.availableMoney.compareTo(totalPrice) < 0) {
//            return false;
//        }
//
//        // Deduct the money from the customer's available money
//        this.availableMoney = this.availableMoney.subtract(totalPrice);
//
//        // Add the good to the customer's basket
//        this.basket.merge(good, amount, Double::sum);
//
//        // Remove the quantity from the shop
//        shop.getAvailableGoods().put(good, availableQuantity - amount);
//
//        // Add the income to the shop's income map
//        shop.getIncomePerDayMap().putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
//        shop.getIncomePerDayMap().merge(LocalDate.now(), totalPrice, BigDecimal::add);
//
//        return true;
//    }


}
