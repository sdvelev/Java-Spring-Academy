package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.model.Shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface for operations related to purchases.
 */
public interface PurchaseServiceAPI {

    /**
     * Serves the next customer in the queue.
     *
     * @param shop the shop where the purchase is made
     * @param cashDesk the cash desk processing the purchase
     * @return the receipt for the processed customer
     * @throws CashDeskClosedException if the cash desk is closed
     * @throws NotEnoughMoneyException if the customer does not have enough money
     * @throws IOException if an I/O error occurs during the process
     */
    Receipt serveNextCustomer(Shop shop, CashDesk cashDesk) throws IOException;

    /**
     * Gets the first customer in the queue.
     *
     * @param cashDesk the cash desk to get the first customer from
     * @return the first customer in the queue
     * @throws CashDeskClosedException if the cash desk is closed
     */
    Customer getFirstCustomer(CashDesk cashDesk) throws CashDeskClosedException;

    /**
     * Returns the goods in the customer's basket to the shop.
     *
     * @param customer the customer whose basket will be returned
     * @param shop the shop to which the goods will be returned
     */
    void returnBasket(Customer customer, Shop shop);

    /**
     * Generates a receipt for the given cash desk and goods map, stores it in txt file and serializes it.
     *
     * @param cashDesk the cash desk processing the purchase
     * @param receiptGoodsMap the map of goods to include in the receipt
     * @param totalAmount the total amount of the receipt
     * @param shop the shop where the purchase is made
     * @return the generated receipt
     * @throws IOException if an I/O error occurs during the process
     */
    Receipt generateReceipt(CashDesk cashDesk, Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap, BigDecimal totalAmount, Shop shop) throws IOException;
}