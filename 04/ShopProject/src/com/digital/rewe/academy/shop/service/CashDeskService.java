package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.Shop;
import com.digital.rewe.academy.shop.exception.CashDeskClosedException;
import com.digital.rewe.academy.shop.exception.NoCustomerWaitingException;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Receipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CashDeskService {
    private static long receiptId = 1;

    public static Receipt serveNextCustomer(CashDesk cashDesk) throws IOException {
        validateCashDesk(cashDesk);
        validateCashDescOpen(cashDesk);
        validateCustomersWaiting(cashDesk);

        Customer nextCustomer = getFirstCustomer(cashDesk);
        Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap = new LinkedHashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<Good, Double> currentEntry : nextCustomer.getBasket().entrySet()) {
            Good currentGood = currentEntry.getKey();
            double currentGoodQuantity = currentEntry.getValue();

            BigDecimal markupPercentage = cashDesk.getShop().getMarkupPercentage(currentGood.getGoodCategory());
            BigDecimal sellingPrice = currentGood.getPurchasePrice().multiply(markupPercentage.add(BigDecimal.ONE));

            if (currentGood.isExpirableGood()) {
                LocalDate expiryDate = currentGood.getExpiryDate();
                if (expiryDate.isBefore(LocalDate.now())) {
                    continue;
                }
                if (ChronoUnit.DAYS.between(LocalDate.now(), expiryDate) <= cashDesk.getShop()
                    .getDaysBeforeExpiryDateDiscount()) {
                    sellingPrice = sellingPrice.multiply(BigDecimal.ONE.subtract(cashDesk.getShop()
                        .getDiscountExpiryDatePercentage()));
                }
            }

            receiptGoodsMap.put(currentGood.getName(), new AbstractMap.SimpleEntry<>(sellingPrice, currentGoodQuantity));
            totalAmount = totalAmount.add(sellingPrice.multiply(BigDecimal.valueOf(currentGoodQuantity)));
        }

        if (totalAmount.compareTo(nextCustomer.getAvailableMoney()) > 0) {
            returnBasket(nextCustomer, nextCustomer.getShop());
            throw new NotEnoughMoneyException("The customer does not have enough money");
        } else {
            nextCustomer.setAvailableMoney(nextCustomer.getAvailableMoney().subtract(totalAmount));
            nextCustomer.getShop().addIncome(totalAmount);
            return generateReceipt(cashDesk, receiptGoodsMap, totalAmount);
        }
    }

    private static Customer getFirstCustomer(CashDesk cashDesk) {
        Customer nextCustomer = cashDesk.getWaitingCustomers().iterator().next();
        cashDesk.getWaitingCustomers().remove(nextCustomer);
        return nextCustomer;
    }

    private static void returnBasket(Customer currentCustomer, Shop shop) {
        currentCustomer.getBasket().entrySet().stream()
            .filter(entry -> !(entry.getKey().isExpirableGood() && entry.getKey().getExpiryDate().isBefore(LocalDate.now())))
            .forEach(entry -> shop.returnGood(entry.getKey(), entry.getValue()));
    }

    private static Receipt generateReceipt(CashDesk cashDesk, Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap,
                                           BigDecimal totalAmount) throws IOException {
        Receipt receiptToHand = new Receipt(receiptId++, cashDesk.getShop().getName(), cashDesk.getWorkingCashier(),
            cashDesk.getCashDeskNumber());
        receiptToHand.addGoods(receiptGoodsMap);
        receiptToHand.setTotalPrice(totalAmount);
        ReceiptService.storeInFile(receiptToHand);
        ReceiptService.serializeReceipt(receiptToHand);
        return receiptToHand;
    }

    private static void validateCashDesk(CashDesk cashDesk) {
        if (cashDesk == null) {
            throw new NullValueException("The provided cash desk cannot be null");
        }
    }

    private static void validateCashDescOpen(CashDesk cashDesk) {
        if (!cashDesk.isOpen()) {
            throw new CashDeskClosedException("The current cash desk is closed");
        }
    }

    private static void validateCustomersWaiting(CashDesk cashDesk) {
        if (cashDesk.getWaitingCustomers().isEmpty()) {
            throw new NoCustomerWaitingException("There is not a customer waiting to be served");
        }
    }
}