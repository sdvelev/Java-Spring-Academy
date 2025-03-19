package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ShopFinancialServiceAPI;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.ShopValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class ShopFinancialService implements ShopFinancialServiceAPI {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private ShopValidator shopValidator;

    public ShopFinancialService(ShopValidator shopValidator) {
        this.shopValidator = shopValidator;
    }

    @Override
    public BigDecimal getCostsForDay(Shop shop, LocalDate targetDate) {
        shopValidator.validateShop(shop);
        shopValidator.validateDate(targetDate);
        return shop.getCostsPerDayMap().getOrDefault(targetDate, BigDecimal.ZERO)
            .add(shouldAddSalary(targetDate) ? getTotalSalaries(shop) : BigDecimal.ZERO)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getCostsForPeriod(Shop shop, LocalDate startDate, LocalDate endDate) {
        shopValidator.validateShop(shop);
        shopValidator.validateDate(startDate);
        shopValidator.validateDate(endDate);
        shopValidator.validateStartDateBeforeEndDate(startDate, endDate);
        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> shop.getCostsPerDayMap().getOrDefault(date, BigDecimal.ZERO)
                .add(shouldAddSalary(date) ? getTotalSalaries(shop) : BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeForDay(Shop shop, LocalDate targetDate) {
        shopValidator.validateShop(shop);
        shopValidator.validateDate(targetDate);
        return shop.getIncomePerDayMap().getOrDefault(targetDate, BigDecimal.ZERO)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeForPeriod(Shop shop, LocalDate startDate, LocalDate endDate) {
        shopValidator.validateShop(shop);
        shopValidator.validateDate(startDate);
        shopValidator.validateDate(endDate);
        shopValidator.validateStartDateBeforeEndDate(startDate, endDate);
        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> shop.getIncomePerDayMap().getOrDefault(date, BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getProfitForDay(Shop shop, LocalDate targetDate) {
        shopValidator.validateShop(shop);
        shopValidator.validateDate(targetDate);
        return this.getIncomeForDay(shop, targetDate).subtract(this.getCostsForDay(shop, targetDate))
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getProfitForPeriod(Shop shop, LocalDate startDate, LocalDate endDate) {
        shopValidator.validateShop(shop);
        shopValidator.validateDate(startDate);
        shopValidator.validateDate(endDate);
        shopValidator.validateStartDateBeforeEndDate(startDate, endDate);
        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> this.getIncomeForDay(shop, date).subtract(this.getCostsForDay(shop, date)))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public boolean shouldAddSalary(LocalDate date) {
        shopValidator.validateDate(date);
        // Check if given date is end of month
        return date.equals(date.withDayOfMonth(date.lengthOfMonth()));
    }

    @Override
    public BigDecimal getTotalSalaries(Shop shop) {
        shopValidator.validateShop(shop);
        return shop.getCashiers().stream()
            .map(Cashier::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}