package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.validation.ShopValidator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Shop implements Serializable {

    private transient ShopValidator shopValidator;

    private long id;
    private String name;
    private int daysBeforeExpiryDateDiscount;
    private BigDecimal discountExpiryDatePercentage;
    private final Map<GoodCategory, BigDecimal> markupPercentages;
    private final Set<Cashier> cashiersSet;
    private final Set<CashDesk> cashDesksSet;
    private final Map<Good, Double> availableGoods;
    private final Map<LocalDate, BigDecimal> costsPerDayMap;
    private final Map<LocalDate, BigDecimal> incomePerDayMap;

    public Shop(long id, String name, int daysBeforeExpiryDateDiscount, BigDecimal discountExpiryDatePercentage,
                ShopValidator shopValidator) {
        this.shopValidator = shopValidator;
        this.id = id;
        this.setName(name);
        this.setDaysBeforeExpiryDateDiscount(daysBeforeExpiryDateDiscount);
        this.setDiscountExpiryDatePercentage(discountExpiryDatePercentage);
        this.markupPercentages = new EnumMap<>(GoodCategory.class);
        this.cashiersSet = new HashSet<>();
        this.cashDesksSet = new HashSet<>();
        this.availableGoods = new HashMap<>();
        this.costsPerDayMap = new HashMap<>();
        this.incomePerDayMap = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDaysBeforeExpiryDateDiscount() {
        return daysBeforeExpiryDateDiscount;
    }

    public BigDecimal getDiscountExpiryDatePercentage() {
        return discountExpiryDatePercentage;
    }

    public Map<GoodCategory, BigDecimal> getMarkupPercentages() {
        return markupPercentages;
    }

    public Set<CashDesk> getCashDesks() {
        return this.cashDesksSet;
    }

    public Set<Cashier> getCashiers() {
        return this.cashiersSet;
    }

    public Map<Good, Double> getAvailableGoods() {
        return availableGoods;
    }

    public Map<LocalDate, BigDecimal> getCostsPerDayMap() {
        return costsPerDayMap;
    }

    public Map<LocalDate, BigDecimal> getIncomePerDayMap() {
        return incomePerDayMap;
    }

    public void setName(String name) {
        shopValidator.validateShopName(name);
        this.name = name;
    }

    public void setDaysBeforeExpiryDateDiscount(int daysBeforeExpiryDateDiscount) {
        shopValidator.validateDaysBeforeExpiryDateDiscount(daysBeforeExpiryDateDiscount);
        this.daysBeforeExpiryDateDiscount = daysBeforeExpiryDateDiscount;
    }

    public void setDiscountExpiryDatePercentage(BigDecimal discountExpiryDatePercentage) {
        shopValidator.validateDiscountExpiryDatePercentage(discountExpiryDatePercentage);
        this.discountExpiryDatePercentage = discountExpiryDatePercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return id == shop.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}