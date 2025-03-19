package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Receipt;

/**
 * Interface for operations related to cash desks.
 */
public interface CashDeskServiceAPI {

//    /**
//     * Checks if the cash desk is open.
//     *
//     * @param cashDesk the cash desk to check
//     * @return true if the cash desk is open, false otherwise
//     */
//    boolean isOpen(CashDesk cashDesk);

//    /**
//     * Closes the cash desk.
//     *
//     * @param cashDesk the cash desk to close
//     */
//    void closeCashDesk(CashDesk cashDesk);

    /**
     * Adds a customer to the waiting queue of the cash desk.
     *
     * @param cashDesk the cash desk to which the customer will be added
     * @param customerToAdd the customer to add
     * @throws CashDeskClosedException if the cash desk is closed
     */
    void addCustomerWaiting(CashDesk cashDesk, Customer customerToAdd);
}