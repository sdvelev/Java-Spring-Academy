package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Good implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

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
        this.setName(name);
        this.setGoodCategory(goodCategory);
        this.setGoodUnit(goodUnit);
        this.setPurchasePrice(purchasePrice);
        this.setDeliveryPercentage(deliveryPercentage);
        this.expiryDate = null;
    }

    public Good(long id, String name, GoodCategory goodCategory, GoodUnit goodUnit, BigDecimal purchasePrice,
                BigDecimal deliveryPercentage, LocalDate expiryDate) {
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
        validateName(name);
        this.name = name;
    }

    public void setGoodCategory(GoodCategory goodCategory) {
        validateGoodCategory(goodCategory);
        this.goodCategory = goodCategory;
    }

    public void setGoodUnit(GoodUnit goodUnit) {
        validateGoodUnit(goodUnit);
        this.goodUnit = goodUnit;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        validatePurchasePrice(purchasePrice);
        this.purchasePrice = purchasePrice;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        validateExpiryDate(expiryDate);
        this.expiryDate = expiryDate;
    }

    public void setDeliveryPercentage(BigDecimal deliveryPercentage) {
        validateDeliveryPercentage(deliveryPercentage);
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

    private void validateName(String name) {
        if (name == null) {
            throw new NullValueException("The name of the good cannot be null");
        }

        if (name.isBlank()) {
            throw new EmptyValueException("The name of the good cannot be empty or blank");
        }
    }

    private void validateGoodCategory(GoodCategory goodCategory) {
        if (goodCategory == null) {
            throw new NullValueException("The category of the good cannot be null");
        }
    }

    private void validateGoodUnit(GoodUnit goodUnit) {
        if (goodUnit == null) {
            throw new NullValueException("The unit of the good cannot be null");
        }
    }

    private void validatePurchasePrice(BigDecimal purchasePrice) {
        if (purchasePrice == null) {
            throw new NullValueException("The purchase price of the good cannot be null");
        }
    }

    private void validateDeliveryPercentage(BigDecimal deliveryPercentage) {
        if (deliveryPercentage == null) {
            throw new NullValueException("The delivery percentage of the good cannot be null");
        }

        if (deliveryPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The delivery percentage of the good cannot be negative");
        }
    }

    private void validateExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new NullValueException("The expiry date of the good (if provided) cannot be null");
        }
    }
}