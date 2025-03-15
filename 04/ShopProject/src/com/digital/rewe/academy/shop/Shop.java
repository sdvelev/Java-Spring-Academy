package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.MarkupPercentagesMissingException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Shop {

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

    public Double loadNewGood(Good good, Double quantity) {
        if (!markupPercentages.containsKey(good.getGoodCategory())) {
            throw new MarkupPercentagesMissingException("There is not defined markup percentage for that good category");
        }

        this.costsPerDayMap.putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        this.costsPerDayMap.merge(LocalDate.now(), good.getPurchasePrice().multiply(BigDecimal.valueOf(quantity)),
            BigDecimal::add);

        return this.availableGoods.merge(good, quantity, Double::sum);
    }

    public void loadNewGoods(Map<Good, Double> newGoodsMap) {
        newGoodsMap.forEach(this::loadNewGood);
    }


}