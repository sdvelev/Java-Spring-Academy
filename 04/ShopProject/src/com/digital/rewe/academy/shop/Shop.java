package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.MarkupPercentagesMissingException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;
import com.digital.rewe.academy.shop.exception.NoSuchGoodException;
import com.digital.rewe.academy.shop.exception.NoSuchQuantityException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Shop implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private long id;
    private String name;

    private int daysBeforeExpiryDateDiscount;
    private BigDecimal discountExpiryDatePercentage;

    private Map<GoodCategory, BigDecimal> markupPercentages;

    private Set<Cashier> cashiersSet;
    private Set<CashDesk> cashDesksSet;

    private Map<Good, Double> availableGoods;

    private Map<LocalDate, BigDecimal> costsPerDayMap;
    private Map<LocalDate, BigDecimal> incomePerDayMap;

    public Shop(long id, String name, int daysBeforeExpiryDateDiscount, BigDecimal discountExpiryDatePercentage) {
        this.id = id;
        this.name = name;
        this.daysBeforeExpiryDateDiscount = daysBeforeExpiryDateDiscount;
        this.discountExpiryDatePercentage = discountExpiryDatePercentage;

        this.markupPercentages = new EnumMap<>(GoodCategory.class);
        this.cashiersSet = new HashSet<>();
        this.cashDesksSet = new HashSet<>();
        this.availableGoods = new HashMap<>();
        this.costsPerDayMap = new HashMap<>();
        this.incomePerDayMap = new HashMap<>();
    }

    public boolean addCashier(Cashier cashierToAdd) {
        return this.cashiersSet.add(cashierToAdd);
    }

    public boolean addCashDesk(CashDesk cashDeskToAdd) {
        return this.cashDesksSet.add(cashDeskToAdd);
    }

    public boolean removeCashier(Cashier cashierToRemove) {
        if (this.cashiersSet.contains(cashierToRemove)) {
            return this.cashiersSet.remove(cashierToRemove);
        }

        throw new NoSuchCashierException("There is not such a customer in the shop");
    }

    public boolean removeCashDesk(CashDesk cashDeskToRemove) {
        if (this.cashDesksSet.contains(cashDeskToRemove)) {
            return this.cashDesksSet.remove(cashDeskToRemove);
        }

        throw new NoSuchCashDeskException("There is not such a cash desk in the shop");
    }

    public Set<CashDesk> getWorkingCashDesks() {
        return this.cashDesksSet.stream()
                                .filter(CashDesk::isOpen)
                                .collect(Collectors.toSet());
    }

    public BigDecimal addMarkupPercentage(GoodCategory goodCategory,  BigDecimal percentage) {
        return this.markupPercentages.put(goodCategory, percentage);
    }

    public BigDecimal getMarkupPercentage(GoodCategory goodCategory) {
        if (!this.markupPercentages.containsKey(goodCategory)) {
            throw new MarkupPercentagesMissingException("There is not defined markup percentage for that good category");
        }

        return this.markupPercentages.get(goodCategory);
    }

    public int getDaysBeforeExpiryDateDiscount() {
        return daysBeforeExpiryDateDiscount;
    }

    public BigDecimal getDiscountExpiryDatePercentage() {
        return discountExpiryDatePercentage;
    }

    public Double loadNewGood(Good good, Double quantity) {

        this.costsPerDayMap.putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        this.costsPerDayMap.merge(LocalDate.now(), good.getPurchasePrice()
            .multiply(BigDecimal.ONE.add(good.getDeliveryPercentage())).multiply(BigDecimal.valueOf(quantity)),
            BigDecimal::add);

        return this.availableGoods.merge(good, quantity, Double::sum);
    }

    public void addIncome(BigDecimal totalAmount) {
        this.incomePerDayMap.putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        this.incomePerDayMap.merge(LocalDate.now(), totalAmount, BigDecimal::add);
    }

    public void loadNewGoods(Map<Good, Double> newGoodsMap) {
        newGoodsMap.forEach(this::loadNewGood);
    }

    public void removeGood(Good good, Double quantity) {
        if (!this.availableGoods.containsKey(good)) {
            throw new NoSuchGoodException("There is not such a good in the shop");
        }

        if (this.availableGoods.get(good).compareTo(quantity) < 0) {
            throw new NoSuchQuantityException("There is not enough quantity from the good");
        } else if (this.availableGoods.get(good).equals(quantity)) {
            this.availableGoods.remove(good);
        } else {
            this.availableGoods.put(good, this.availableGoods.get(good) - quantity);
        }
    }

    public void returnGood(Good good, Double quantity) {
        this.availableGoods.merge(good, quantity, Double::sum);
    }

    public BigDecimal getCostsForDay(LocalDate targetDate) {
        return this.costsPerDayMap.getOrDefault(targetDate, BigDecimal.ZERO)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public BigDecimal getIncomePerDay(LocalDate targetDate) {
        return this.incomePerDayMap.getOrDefault(targetDate, BigDecimal.ZERO)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public BigDecimal getProfitPerDay(LocalDate targetDate) {
        return this.getIncomePerDay(targetDate).subtract(this.getCostsForDay(targetDate))
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}