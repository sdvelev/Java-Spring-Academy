package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NoCustomerWaitingException;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CashDesk {
    private long id;
    private int cashDeskNumber;
    private Shop shop;
    private Optional<Cashier> workingCashier;
    private final Set<Customer> waitingCustomers;

    public CashDesk(long id, int cashDeskNumber, Shop shop) {
        this.id = id;
        this.cashDeskNumber = cashDeskNumber;
        this.shop = shop;
        this.workingCashier = Optional.empty();
        this.waitingCustomers = new LinkedHashSet<>();
    }

    public void openCashDesk(Cashier workingCashier) {
        validateCashierIsNull(workingCashier);
        this.workingCashier = Optional.of(workingCashier);
    }

    public void closeCashDesk() {
        this.workingCashier = Optional.empty();
    }

    public boolean isOpen() {
        return this.workingCashier.isPresent();
    }

    public void addCustomerWaiting(Customer customerToAdd) {
        if (isOpen()) {
            validateCustomerIsNull(customerToAdd);
            this.waitingCustomers.add(customerToAdd);
        } else {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }

    private void validateCashierIsNull(Cashier cashier) {
        if (cashier == null) {
            throw new NullValueException("The cashier argument cannot be null");
        }
    }

    private void validateCustomerIsNull(Customer customer) {
        if (customer == null) {
            throw new NullValueException("The customer argument cannot be null");
        }
    }

    public Receipt serveNextCustomer() throws IOException {
        if (!this.isOpen()) {
            throw new CashDeskClosedException("The current cash desk is closed");
        }

        if (this.waitingCustomers.isEmpty()) {
            throw new NoCustomerWaitingException("There is not any customer waiting to be served");
        }

        Customer nextCustomer = this.waitingCustomers.iterator().next();
        this.waitingCustomers.remove(nextCustomer);

        Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap = new LinkedHashMap<>();

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<Good, Double> currentEntry : nextCustomer.getBasket().entrySet()) {
            BigDecimal markupPercentage = shop.getMarkupPercentage(currentEntry.getKey().getGoodCategory());
            BigDecimal sellingPrice = currentEntry.getKey().getPurchasePrice().multiply(markupPercentage.add(BigDecimal.ONE));

            if (currentEntry.getKey().isExpirableGood()) {
                LocalDate expiryDate = currentEntry.getKey().getExpiryDate();
                if (expiryDate.isBefore(LocalDate.now())) {
                    continue;
                }

                if (ChronoUnit.DAYS.between(LocalDate.now(), expiryDate) <= shop.getDaysBeforeExpiryDateDiscount()) {
                    sellingPrice = sellingPrice.multiply(BigDecimal.ONE.subtract(shop.getDiscountExpiryDatePercentage()));
                }
            }

            receiptGoodsMap.put(currentEntry.getKey().getName(), new AbstractMap.SimpleEntry<>(sellingPrice, currentEntry.getValue()));
            totalAmount = totalAmount.add(sellingPrice.multiply(BigDecimal.valueOf(currentEntry.getValue())));
        }

        if (totalAmount.compareTo(nextCustomer.getAvailableMoney()) > 0) {
            for (Map.Entry<Good, Double> currentEntry : nextCustomer.getBasket().entrySet()) {
                if (currentEntry.getKey().isExpirableGood() && currentEntry.getKey().getExpiryDate().isBefore(LocalDate.now())) {
                    continue;
                }
                this.shop.returnGood(currentEntry.getKey(), currentEntry.getValue());
            }

            throw new NotEnoughMoneyException("The client does not have enough money");
        } else {
            nextCustomer.setAvailableMoney(nextCustomer.getAvailableMoney().subtract(totalAmount));
            shop.addIncome(totalAmount);

            Receipt receiptToHand = new Receipt(1, this.shop.getName(), this.workingCashier.get(),
                this.cashDeskNumber);
            receiptToHand.addGoods(receiptGoodsMap);
            receiptToHand.setTotalPrice(totalAmount);
            ReceiptService.storeInFile(receiptToHand);
            ReceiptService.serializeReceipt(receiptToHand);
            return receiptToHand;
        }
    }
}