package com.digital.rewe.academy.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Good {
    private long id;
    private String name;
    private GoodCategory goodCategory;
    private GoodUnit goodUnit;
    private BigDecimal purchasePrice;
    private Optional<LocalDate> expiryDate;

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice) {
        this.id = id;
        this.name = name;
        this.goodCategory = goodCategory;
        this.purchasePrice = purchasePrice;
        this.expiryDate = Optional.empty();
    }

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice,
                LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.goodCategory = goodCategory;
        this.purchasePrice = purchasePrice;
        this.expiryDate = Optional.of(expiryDate);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GoodCategory getGoodCategory() {
        return goodCategory;
    }

    public GoodUnit getGoodUnit() {
        return goodUnit;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public boolean isExpirableGood() {
        return expiryDate.isPresent();
    }

    public LocalDate getExpiryDate() {
        return expiryDate.orElse(null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoodCategory(GoodCategory goodCategory) {
        this.goodCategory = goodCategory;
    }

    public void setGoodUnit(GoodUnit goodUnit) {
        this.goodUnit = goodUnit;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = Optional.of(expiryDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return id == good.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}