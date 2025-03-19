package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.validation.GoodValidator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Good implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private transient GoodValidator goodValidator;

    private long id;
    private String name;
    private GoodCategory goodCategory;
    private GoodUnit goodUnit;
    private BigDecimal purchasePrice;
    private BigDecimal deliveryPercentage;
    private LocalDate expiryDate;

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice,
                BigDecimal deliveryPercentage, GoodValidator goodValidator) {
        this.goodValidator = goodValidator;
        this.id = id;
        this.setName(name);
        this.setGoodCategory(goodCategory);
        this.setGoodUnit(goodUnit);
        this.setPurchasePrice(purchasePrice);
        this.setDeliveryPercentage(deliveryPercentage);
        this.expiryDate = null;
    }

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice,
                BigDecimal deliveryPercentage, GoodValidator goodValidator, LocalDate expiryDate) {
        this.goodValidator = goodValidator;
        this.id = id;
        this.setName(name);
        this.setGoodCategory(goodCategory);
        this.setGoodUnit(goodUnit);
        this.setPurchasePrice(purchasePrice);
        this.setDeliveryPercentage(deliveryPercentage);
        this.setExpiryDate(expiryDate);
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
        return purchasePrice.setScale(ROUNDING_SCALE_FACTOR,  RoundingMode.HALF_UP);
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public BigDecimal getDeliveryPercentage() {
        return deliveryPercentage;
    }

    public void setName(String name) {
        goodValidator.validateName(name);
        this.name = name;
    }

    public void setGoodCategory(GoodCategory goodCategory) {
        goodValidator.validateGoodCategory(goodCategory);
        this.goodCategory = goodCategory;
    }

    public void setGoodUnit(GoodUnit goodUnit) {
        goodValidator.validateGoodUnit(goodUnit);
        this.goodUnit = goodUnit;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        goodValidator.validatePurchasePrice(purchasePrice);
        this.purchasePrice = purchasePrice;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        goodValidator.validateExpiryDate(expiryDate);
        this.expiryDate = expiryDate;
    }

    public void setDeliveryPercentage(BigDecimal deliveryPercentage) {
        goodValidator.validateDeliveryPercentage(deliveryPercentage);
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
            ", purchasePrice=" + purchasePrice.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP) +
            ", deliveryPercentage=" + deliveryPercentage +
            ", expiryDate=" + expiryDate +
            '}';
    }
}