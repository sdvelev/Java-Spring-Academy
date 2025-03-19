package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.exception.*;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface for operations related to shop management.
 */
public interface ShopOperationsServiceAPI {

    /**
     * Gets the markup percentage for a specific good category.
     *
     * @param shop the shop for which to get the markup percentage
     * @param goodCategory the good category for which to get the markup percentage
     * @return the markup percentage for the specified good category
     * @throws MarkupPercentagesMissingException if there is no defined markup percentage for the provided good category
     * @throws NullValueException if the good is null
     */
    BigDecimal getMarkupPercentage(Shop shop, GoodCategory goodCategory);

    /**
     * Adds a markup percentage for a specific good category.
     *
     * @param shop the shop to which the markup percentage will be added
     * @param goodCategory the good category for which to add the markup percentage
     * @param percentage the markup percentage to add
     * @throws NullValueException if any of the provided arguments is null
     * @throws NegativeValueException if percentage is negative
     */
    void addMarkupPercentage(Shop shop, GoodCategory goodCategory, BigDecimal percentage);

    /**
     * Adds a cashier to the shop.
     *
     * @param shop the shop to which the cashier will be added
     * @param cashierToAdd the cashier to add
     * @throws NullValueException if any of the provided arguments is null
     */
    void addCashier(Shop shop, Cashier cashierToAdd);

    /**
     * Removes a cashier from the shop.
     *
     * @param shop the shop from which the cashier will be removed
     * @param cashierToRemove the cashier to remove
     * @throws NoSuchCashierException if there is no such cashier in the shop
     * @throws NullValueException if any of the provided arguments is null
     */
    void removeCashier(Shop shop, Cashier cashierToRemove);

    /**
     * Adds a cash desk to the shop.
     *
     * @param shop the shop to which the cash desk will be added
     * @param cashDeskToAdd the cash desk to add
     * @throws NullValueException if any of the provided arguments is null
     */
    void addCashDesk(Shop shop, CashDesk cashDeskToAdd);

    /**
     * Removes a cash desk from the shop.
     *
     * @param shop the shop from which the cash desk will be removed
     * @param cashDeskToRemove the cash desk to remove
     * @throws NoSuchCashDeskException if there is no such cash desk in the shop
     * @throws NullValueException if any of the provided arguments is null
     */
    void removeCashDesk(Shop shop, CashDesk cashDeskToRemove);

    /**
     * Removes a good from the shop.
     *
     * @param shop the shop from which the good will be removed
     * @param good the good to remove
     * @param quantity the quantity of the good to remove
     * @throws NoSuchGoodException if there is no such good in the shop
     * @throws NoSuchQuantityException if there is not enough quantity of the good
     * @throws NullValueException if any of the provided arguments is null
     * @throws NegativeValueException if quantity is negative
     */
    void removeGood(Shop shop, Good good, double quantity);

    /**
     * Returns a good to the shop.
     *
     * @param shop the shop to which the good will be returned
     * @param good the good to return
     * @param quantity the quantity of the good to return
     * @throws NullValueException if any of the provided arguments is null
     * @throws NegativeValueException if quantity is negative
     */
    void returnGood(Shop shop, Good good, Double quantity);

    /**
     * Loads a new good into the shop.
     *
     * @param shop the shop to which the good will be loaded
     * @param good the good to load
     * @param quantity the quantity of the good to load
     * @throws NullValueException if any of the provided arguments is null
     * @throws NegativeValueException if quantity is negative
     */
    void loadNewGood(Shop shop, Good good, double quantity);

    /**
     * Loads new goods into the shop.
     *
     * @param shop the shop to which the goods will be loaded
     * @param newGoodsMap the map of goods to load
     * @throws NullValueException if the provided map with new goods is null
     */
    void loadNewGoods(Shop shop, Map<Good, Double> newGoodsMap);

    /**
     * Adds income to the shop.
     *
     * @param shop the shop to which the income will be added
     * @param totalAmount the total amount of income to add
     * @throws NullValueException if any of the provided arguments is null
     * @throws NegativeValueException if totalAmount is negative
     */
    void addIncome(Shop shop, BigDecimal totalAmount);
}