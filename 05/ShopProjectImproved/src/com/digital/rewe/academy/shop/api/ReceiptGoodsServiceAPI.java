package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Receipt;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface for operations related to receipt goods.
 */
public interface ReceiptGoodsServiceAPI {

    /**
     * Adds good to the receipt.
     *
     * @param receipt the receipt to which the goods will be added
     * @param goodName the name of the good that will be added
     * @param finalPrice the final price of the good that will be added
     * @param quantity the quantity of the good that will be added
     * @throws NullValueException if any of the provided values are null
     * @throws EmptyValueException if the name of the good is empty or blank
     * @throws NegativeValueException if the quantity of the good is negative
     */
    void addGood(Receipt receipt, String goodName, BigDecimal finalPrice, double quantity);

    /**
     * Adds goods to the receipt.
     *
     * @param receipt the receipt to which the goods will be added
     * @param goodWithQuantityMap the map of goods to add, where the key is the good name and the value is a pair of price and quantity
     * @throws NullValueException if any of the provided values are null
     */
    void addGoods(Receipt receipt, Map<String, Map.Entry<BigDecimal, Double>> goodWithQuantityMap);

    /**
     * Removes goods from the receipt.
     *
     * @param receipt the receipt from which the goods will be removed
     * @param goodName the name of the good
     * @throws NullValueException if the name of the good is null
     * @throws EmptyValueException if the name of the good is empty or blank
     */
    void removeGood(Receipt receipt, String goodName);
}