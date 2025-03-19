package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ShopOperationsServiceAPI;
import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.exception.MarkupPercentagesMissingException;
import com.digital.rewe.academy.shop.exception.NoSuchCashDeskException;
import com.digital.rewe.academy.shop.exception.NoSuchCashierException;
import com.digital.rewe.academy.shop.exception.NoSuchGoodException;
import com.digital.rewe.academy.shop.exception.NoSuchQuantityException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.ShopValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ShopOperationsService implements ShopOperationsServiceAPI {

    private ShopValidator shopValidator;

    public ShopOperationsService(ShopValidator shopValidator) {
        this.shopValidator = shopValidator;
    }

    @Override
    public BigDecimal getMarkupPercentage(Shop shop, GoodCategory goodCategory) {
        shopValidator.validateGoodCategory(goodCategory);
        if (!shop.getMarkupPercentages().containsKey(goodCategory)) {
            throw new MarkupPercentagesMissingException("There is not a defined markup percentage for the provided good category");
        }

        return shop.getMarkupPercentages().get(goodCategory);
    }

    @Override
    public void addMarkupPercentage(Shop shop, GoodCategory goodCategory, BigDecimal percentage) {
        shopValidator.validateGoodCategory(goodCategory);
        shopValidator.validateMarkupPercentage(percentage);
        shop.getMarkupPercentages().put(goodCategory, percentage);
    }

    @Override
    public void addCashier(Shop shop, Cashier cashierToAdd) {
        shopValidator.validateCashier(cashierToAdd);
        shop.getCashiers().add(cashierToAdd);
    }

    @Override
    public void removeCashier(Shop shop, Cashier cashierToRemove) {
        shopValidator.validateCashier(cashierToRemove);
        if (!shop.getCashiers().contains(cashierToRemove)) {
            throw new NoSuchCashierException("There is not such a cashier in the shop");
        }

        shop.getCashiers().remove(cashierToRemove);
    }

    @Override
    public void addCashDesk(Shop shop, CashDesk cashDeskToAdd) {
        shopValidator.validateCashDesk(cashDeskToAdd);
        shop.getCashDesks().add(cashDeskToAdd);
    }

    @Override
    public void removeCashDesk(Shop shop, CashDesk cashDeskToRemove) {
        shopValidator.validateCashDesk(cashDeskToRemove);
        if (!shop.getCashDesks().contains(cashDeskToRemove)) {
            throw new NoSuchCashDeskException("There is not such a cash desk in the shop");
        }

        shop.getCashDesks().remove(cashDeskToRemove);
    }

    @Override
    public void removeGood(Shop shop, Good good, double quantity) {
        shopValidator.validateGood(good);
        shopValidator.validateQuantity(quantity);

        if (!shop.getAvailableGoods().containsKey(good)) {
            throw new NoSuchGoodException("There is not such a good in the shop");
        }

        if (shop.getAvailableGoods().get(good).compareTo(quantity) < 0) {
            throw new NoSuchQuantityException("There is not enough quantity from the good");
        } else if (shop.getAvailableGoods().get(good).equals(quantity)) {
            shop.getAvailableGoods().remove(good);
        } else {
            shop.getAvailableGoods().put(good, shop.getAvailableGoods().get(good) - quantity);
        }
    }

    @Override
    public void returnGood(Shop shop, Good good, Double quantity) {
        shopValidator.validateGood(good);
        shopValidator.validateQuantity(quantity);
        shop.getAvailableGoods().putIfAbsent(good, 0.);
        shop.getAvailableGoods().merge(good, quantity, Double::sum);
    }

    @Override
    public void loadNewGood(Shop shop, Good good, double quantity) {
        shopValidator.validateGood(good);
        shopValidator.validateQuantity(quantity);
        shop.getCostsPerDayMap().putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        shop.getCostsPerDayMap().merge(LocalDate.now(), good.getPurchasePrice()
            .multiply(BigDecimal.ONE.add(good.getDeliveryPercentage()))
            .multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);

        shop.getAvailableGoods().merge(good, quantity, Double::sum);
    }

    @Override
    public void loadNewGoods(Shop shop, Map<Good, Double> newGoodsMap) {
        if (newGoodsMap == null) {
            throw new NullValueException("The provided map with new goods cannot be null");
        }

        for (Map.Entry<Good, Double> entry : newGoodsMap.entrySet()) {
            loadNewGood(shop, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addIncome(Shop shop, BigDecimal totalAmount) {
        shopValidator.validateAmount(totalAmount);
        shop.getIncomePerDayMap().putIfAbsent(LocalDate.now(), BigDecimal.ZERO);
        shop.getIncomePerDayMap().merge(LocalDate.now(), totalAmount, BigDecimal::add);
    }
}