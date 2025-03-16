package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NullValueException;


import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class CashDesk implements Serializable {
    private long id;
    private int cashDeskNumber;
    private Shop shop;
    private Cashier workingCashier;
    private final Set<Customer> waitingCustomers;

    public CashDesk(long id, int cashDeskNumber, Shop shop) {
        this.id = id;
        this.cashDeskNumber = cashDeskNumber;
        this.shop = shop;
        this.workingCashier = null;
        this.waitingCustomers = new LinkedHashSet<>();
    }

    public long getId() {
        return id;
    }

    public int getCashDeskNumber() {
        return cashDeskNumber;
    }

    public Cashier getWorkingCashier() {
        return workingCashier;
    }

    public Set<Customer> getWaitingCustomers() {
        return waitingCustomers;
    }

    public Shop getShop() {
        return shop;
    }

    public void setCashDeskNumber(int cashDeskNumber) {
        this.cashDeskNumber = cashDeskNumber;
    }

    public void openCashDesk(Cashier workingCashier) {
        validateCashierIsNull(workingCashier);
        this.workingCashier = workingCashier;
    }

    public void closeCashDesk() {
        this.workingCashier = null;
    }

    public boolean isOpen() {
        return this.workingCashier != null;
    }

    public void addCustomerWaiting(Customer customerToAdd) {
        if (isOpen()) {
            validateCustomerIsNull(customerToAdd);
            this.waitingCustomers.add(customerToAdd);
        } else {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }

//    public Receipt serveNextCustomer() throws IOException {
//        if (!this.isOpen()) {
//            throw new CashDeskClosedException("The current cash desk is closed");
//        }
//
//        if (this.waitingCustomers.isEmpty()) {
//            throw new NoCustomerWaitingException("There is not any customer waiting to be served");
//        }
//
//        Customer nextCustomer = this.waitingCustomers.iterator().next();
//        this.waitingCustomers.remove(nextCustomer);
//
//        Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap = new LinkedHashMap<>();
//
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        for (Map.Entry<Good, Double> currentEntry : nextCustomer.getBasket().entrySet()) {
//            BigDecimal markupPercentage = shop.getMarkupPercentage(currentEntry.getKey().getGoodCategory());
//            BigDecimal sellingPrice = currentEntry.getKey().getPurchasePrice().multiply(markupPercentage.add(BigDecimal.ONE));
//
//            if (currentEntry.getKey().isExpirableGood()) {
//                LocalDate expiryDate = currentEntry.getKey().getExpiryDate();
//                if (expiryDate.isBefore(LocalDate.now())) {
//                    continue;
//                }
//
//                if (ChronoUnit.DAYS.between(LocalDate.now(), expiryDate) <= shop.getDaysBeforeExpiryDateDiscount()) {
//                    sellingPrice = sellingPrice.multiply(BigDecimal.ONE.subtract(shop.getDiscountExpiryDatePercentage()));
//                }
//            }
//
//            receiptGoodsMap.put(currentEntry.getKey().getName(), new AbstractMap.SimpleEntry<>(sellingPrice, currentEntry.getValue()));
//            totalAmount = totalAmount.add(sellingPrice.multiply(BigDecimal.valueOf(currentEntry.getValue())));
//        }
//
//        if (totalAmount.compareTo(nextCustomer.getAvailableMoney()) > 0) {
//            for (Map.Entry<Good, Double> currentEntry : nextCustomer.getBasket().entrySet()) {
//                if (currentEntry.getKey().isExpirableGood() && currentEntry.getKey().getExpiryDate().isBefore(LocalDate.now())) {
//                    continue;
//                }
//                this.shop.returnGood(currentEntry.getKey(), currentEntry.getValue());
//            }
//
//            throw new NotEnoughMoneyException("The client does not have enough money");
//        } else {
//            nextCustomer.setAvailableMoney(nextCustomer.getAvailableMoney().subtract(totalAmount));
//            shop.addIncome(totalAmount);
//
//            Receipt receiptToHand = new Receipt(1, this.shop.getName(), this.workingCashier, this.cashDeskNumber);
//            receiptToHand.addGoods(receiptGoodsMap);
//            receiptToHand.setTotalPrice(totalAmount);
//            ReceiptService.storeInFile(receiptToHand);
//            ReceiptService.serializeReceipt(receiptToHand);
//            return receiptToHand;
//        }
//    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CashDesk cashDesk = (CashDesk) o;
        return id == cashDesk.id && Objects.equals(shop, cashDesk.shop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shop);
    }

    @Override
    public String toString() {
        return "CashDesk{" +
            "id=" + id +
            ", cashDeskNumber=" + cashDeskNumber +
            ", workingCashier=" + workingCashier +
            '}';
    }
}