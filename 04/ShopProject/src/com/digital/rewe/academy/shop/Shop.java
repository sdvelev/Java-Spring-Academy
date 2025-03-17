package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.exception.CashDeskDifferentException;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.EndDateBeforeStartDateException;
import com.digital.rewe.academy.shop.exception.MarkupPercentagesMissingException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;
import com.digital.rewe.academy.shop.exception.NoSuchGoodException;
import com.digital.rewe.academy.shop.exception.NoSuchQuantityException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Shop implements FinancialOperationsAPI, Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

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

    public Shop(long id, String name, int daysBeforeExpiryDateDiscount, BigDecimal discountExpiryDatePercentage) {
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

    public BigDecimal getMarkupPercentage(GoodCategory goodCategory) {
        validateGoodCategory(goodCategory);
        if (!this.markupPercentages.containsKey(goodCategory)) {
            throw new MarkupPercentagesMissingException("There is not a defined markup percentage for the provided good category");
        }

        return this.markupPercentages.get(goodCategory);
    }

    public Set<CashDesk> getWorkingCashDesks() {
        return this.cashDesksSet.stream()
            .filter(CashDesk::isOpen)
            .collect(Collectors.toSet());
    }

    public Set<CashDesk> getCashiers() {
        return this.cashDesksSet;
    }

    public Map<Good, Double> getAvailableGoods() {
        return availableGoods;
    }

    public void setName(String name) {
        validateShopName(name);
        this.name = name;
    }

    public void setDaysBeforeExpiryDateDiscount(int daysBeforeExpiryDateDiscount) {
        validateDaysBeforeExpiryDateDiscount(daysBeforeExpiryDateDiscount);
        this.daysBeforeExpiryDateDiscount = daysBeforeExpiryDateDiscount;
    }

    public void setDiscountExpiryDatePercentage(BigDecimal discountExpiryDatePercentage) {
        validateDiscountExpiryDatePercentage(discountExpiryDatePercentage);
        this.discountExpiryDatePercentage = discountExpiryDatePercentage;
    }

    public void addMarkupPercentage(GoodCategory goodCategory, BigDecimal percentage) {
        validateGoodCategory(goodCategory);
        validateMarkupPercentage(percentage);
        this.markupPercentages.put(goodCategory, percentage);
    }

    public void addCashier(Cashier cashierToAdd) {
        validateCashier(cashierToAdd);
        this.cashiersSet.add(cashierToAdd);
    }

    public void removeCashier(Cashier cashierToRemove) {
        validateCashier(cashierToRemove);
        if (!this.cashiersSet.contains(cashierToRemove)) {
            throw new NoSuchCashierException("There is not such a cashier in the shop");
        }

        this.cashiersSet.remove(cashierToRemove);
    }

    public void addCashDesk(CashDesk cashDeskToAdd) {
        validateCashDesk(cashDeskToAdd);
        this.cashDesksSet.add(cashDeskToAdd);
    }

    public void removeCashDesk(CashDesk cashDeskToRemove) {
        validateCashDesk(cashDeskToRemove);
        if (!this.cashDesksSet.contains(cashDeskToRemove)) {
            throw new NoSuchCashDeskException("There is not such a cash desk in the shop");
        }

        this.cashDesksSet.remove(cashDeskToRemove);
    }

    public void loadNewGood(Good good, double quantity) {
        validateGood(good);
        validateQuantity(quantity);
        this.costsPerDayMap.putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        this.costsPerDayMap.merge(LocalDate.now(), good.getPurchasePrice()
            .multiply(BigDecimal.ONE.add(good.getDeliveryPercentage()))
            .multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);

        this.availableGoods.merge(good, quantity, Double::sum);
    }

    public void loadNewGoods(Map<Good, Double> newGoodsMap) {
        if (newGoodsMap == null) {
            throw new NullValueException("The provided map with new goods cannot be null");
        }

        newGoodsMap.forEach(this::loadNewGood);
    }

    public void addIncome(BigDecimal totalAmount) {
        validateAmount(totalAmount);
        this.incomePerDayMap.putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        this.incomePerDayMap.merge(LocalDate.now(), totalAmount, BigDecimal::add);
    }

    public void removeGood(Good good, double quantity) {
        validateGood(good);
        validateQuantity(quantity);

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
        validateGood(good);
        validateQuantity(quantity);
        this.availableGoods.putIfAbsent(good, 0.);
        this.availableGoods.merge(good, quantity, Double::sum);
    }

    @Override
    public BigDecimal getCostsForDay(LocalDate targetDate) {
        validateDate(targetDate);
        return this.costsPerDayMap.getOrDefault(targetDate, BigDecimal.ZERO)
            .add(shouldAddSalary(targetDate) ? getTotalSalaries() : BigDecimal.ZERO)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getCostsForPeriod(LocalDate startDate, LocalDate endDate) {
        validateDate(startDate);
        validateDate(endDate);
        validateStartDateBeforeEndDate(startDate, endDate);
        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> this.costsPerDayMap.getOrDefault(date, BigDecimal.ZERO)
                .add(shouldAddSalary(date) ? getTotalSalaries() : BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeForDay(LocalDate targetDate) {
        validateDate(targetDate);
        return this.incomePerDayMap.getOrDefault(targetDate, BigDecimal.ZERO)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeForPeriod(LocalDate startDate, LocalDate endDate) {
        validateDate(startDate);
        validateDate(endDate);
        validateStartDateBeforeEndDate(startDate, endDate);
        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> this.incomePerDayMap.getOrDefault(date, BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getProfitForDay(LocalDate targetDate) {
        validateDate(targetDate);
        return this.getIncomeForDay(targetDate).subtract(this.getCostsForDay(targetDate))
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getProfitForPeriod(LocalDate startDate, LocalDate endDate) {
        validateDate(startDate);
        validateDate(endDate);
        validateStartDateBeforeEndDate(startDate, endDate);
        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> this.getIncomeForDay(date).subtract(this.getCostsForDay(date)))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
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

    private boolean shouldAddSalary(LocalDate date) {
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }

    private BigDecimal getTotalSalaries() {
        return this.cashiersSet.stream()
            .map(Cashier::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateShopName(String shopName) {
        if (shopName == null) {
            throw new NullValueException("The name of the shop cannot be null");
        }

        if (shopName.isBlank()) {
            throw new EmptyValueException("The name of the shop cannot be empty or blank");
        }
    }

    private void validateDaysBeforeExpiryDateDiscount(int daysBeforeExpiryDateDiscount) {
        if (daysBeforeExpiryDateDiscount < 0) {
            throw new NegativeValueException("The days before expiry date discount cannot be negative");
        }
    }

    private void validateDiscountExpiryDatePercentage(BigDecimal discountExpiryDatePercentage) {
        if (discountExpiryDatePercentage == null) {
            throw new NullValueException("The discount expiry date percentage cannot be null");
        }

        if (discountExpiryDatePercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The discount expiry date percentage cannot be negative");
        }
    }

    private void validateGoodCategory(GoodCategory goodCategory) {
        if (goodCategory == null) {
            throw new NullValueException("The provided category of good cannot be null");
        }
    }

    private void validateCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullValueException("The provided cashier cannot be null");
        }
    }

    private void validateMarkupPercentage(BigDecimal markupPercentage) {
        if (discountExpiryDatePercentage == null) {
            throw new NullValueException("The provided markup percentage cannot be null");
        }

        if (discountExpiryDatePercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The provided markup percentage cannot be negative");
        }
    }

    private void validateCashDesk(CashDesk cashDesk) {
        if (cashDesk == null) {
            throw new NullValueException("The provided cash desk cannot be null");
        }

        if (!cashDesk.getShop().getName().equals(this.name)) {
            throw new CashDeskDifferentException("The provided cash desk cannot be assigned to different shop");
        }
    }

    private void validateGood(Good good) {
        if (good == null) {
            throw new NullValueException("The provided good cannot be null");
        }
    }

    private void validateQuantity(double quantity) {
        if (quantity <= 0) {
            throw new NegativeValueException("The provided quantity must be positive");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new NullValueException("The provided amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The provided amount cannot be negative");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new NullValueException("The provided date cannot be null");
        }
    }

    private void validateStartDateBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new EndDateBeforeStartDateException("The provided end date is before the provided start date");
        }
    }
}