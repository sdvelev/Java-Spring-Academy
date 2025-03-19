package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;

/**
 * Interface for operations related to customers.
 */
public interface CustomerServiceAPI {

    /**
     * Adds a good to the customer's basket.
     *
     * @param customer the customer to whom the good will be added
     * @param shop the shop from which the good will be taken
     * @param good the good to add
     * @param amount the amount of the good to add
     * @throws NullValueException if any of the provided values are null
     */
    void addToBasket(Customer customer, Shop shop, Good good, double amount);

    /**
     * Removes a good from the customer's basket.
     *
     * @param customer the customer from whom the good will be removed
     * @param shop the shop to which the good will be returned
     * @param good the good to remove
     * @param amount the amount of the good to remove
     * @return true if the good was successfully removed, false otherwise
     * @throws NullValueException if any of the provided values are null
     * @throws NegativeValueException if the provided amount is negative
     */
    boolean removeFromBasket(Customer customer, Shop shop, Good good, double amount);
}