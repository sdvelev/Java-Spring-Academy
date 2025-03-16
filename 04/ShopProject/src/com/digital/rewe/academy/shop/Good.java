package com.digital.rewe.academy.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Good implements Serializable {
    private long id;
    private String name;
    private GoodCategory goodCategory;
    private GoodUnit goodUnit;
    private BigDecimal purchasePrice;
    private BigDecimal deliveryPercentage;
    private LocalDate expiryDate;

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice,
                BigDecimal deliveryPercentage) {
        this.id = id;
        this.name = name;
        this.goodCategory = goodCategory;
        this.goodUnit = goodUnit;
        this.purchasePrice = purchasePrice;
        this.deliveryPercentage = deliveryPercentage;
        this.expiryDate = null;
    }

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice,
                BigDecimal deliveryPercentage, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.goodCategory = goodCategory;
        this.goodUnit = goodUnit;
        this.purchasePrice = purchasePrice;
        this.deliveryPercentage = deliveryPercentage;
        this.expiryDate = expiryDate;

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
        return expiryDate != null;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public BigDecimal getDeliveryPercentage() {
        return deliveryPercentage;
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
        this.expiryDate = expiryDate;
    }

    public void setDeliveryPercentage(BigDecimal deliveryPercentage) {
        this.deliveryPercentage = deliveryPercentage;
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

    @Override
    public String toString() {
        return "Good{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", goodCategory=" + goodCategory +
            ", goodUnit=" + goodUnit +
            ", purchasePrice=" + purchasePrice +
            ", deliveryPercentage=" + deliveryPercentage +
            ", expiryDate=" + expiryDate +
            '}';
    }
}