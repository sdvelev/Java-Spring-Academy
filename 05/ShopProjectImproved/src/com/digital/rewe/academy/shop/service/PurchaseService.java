package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.PurchaseServiceAPI;
import com.digital.rewe.academy.shop.api.ReceiptGoodsServiceAPI;
import com.digital.rewe.academy.shop.api.ReceiptSerializeDeserializeServiceAPI;
import com.digital.rewe.academy.shop.api.ReceiptStoreInFileServiceAPI;
import com.digital.rewe.academy.shop.api.ShopOperationsServiceAPI;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import com.digital.rewe.academy.shop.validation.ShopValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PurchaseService implements PurchaseServiceAPI {
    private static long receiptId = 1;

    private ShopOperationsServiceAPI shopService;
    private ReceiptGoodsServiceAPI receiptGoodsService;
    private ReceiptStoreInFileServiceAPI receiptStoreInFileService;
    private ReceiptSerializeDeserializeServiceAPI receiptSerializeDeserializeService;

    private CashDeskValidator cashDeskValidator;
    private ReceiptValidator receiptValidator;
    private ShopValidator shopValidator;

    public PurchaseService(CashDeskValidator cashDeskValidator, ReceiptValidator receiptValidator, ShopValidator shopValidator) {
        this.cashDeskValidator = cashDeskValidator;
        this.receiptValidator = receiptValidator;
        this.shopValidator = shopValidator;
        this.shopService = new ShopOperationsService(shopValidator);
        this.receiptGoodsService = new ReceiptGoodsService(receiptValidator);
        this.receiptStoreInFileService = new ReceiptStoreInFileService(receiptValidator);
        this.receiptSerializeDeserializeService = new ReceiptSerializeDeserializeService(receiptValidator);
    }

    @Override
    public Receipt serveNextCustomer(Shop shop, CashDesk cashDesk) throws IOException {
        cashDeskValidator.validateCashDesk(cashDesk);
        cashDeskValidator.validateCashDescOpen(cashDesk);
        cashDeskValidator.validateCustomersWaiting(cashDesk);

        Customer nextCustomer = getFirstCustomer(cashDesk);
        Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap = new LinkedHashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<Good, Double> currentEntry : nextCustomer.getBasket().entrySet()) {
            Good currentGood = currentEntry.getKey();
            double currentGoodQuantity = currentEntry.getValue();

            BigDecimal markupPercentage = shopService.getMarkupPercentage(shop, currentGood.getGoodCategory());
            BigDecimal sellingPrice = currentGood.getPurchasePrice().multiply(markupPercentage.add(BigDecimal.ONE));

            if (currentGood.getExpiryDate() != null) {
                LocalDate expiryDate = currentGood.getExpiryDate();
                if (expiryDate.isBefore(LocalDate.now())) {
                    continue;
                }
                if (ChronoUnit.DAYS.between(LocalDate.now(), expiryDate) <= shop.getDaysBeforeExpiryDateDiscount()) {
                    sellingPrice = sellingPrice.multiply(BigDecimal.ONE.subtract(shop.getDiscountExpiryDatePercentage()));
                }
            }

            receiptGoodsMap.put(currentGood.getName(), new AbstractMap.SimpleEntry<>(sellingPrice, currentGoodQuantity));
            totalAmount = totalAmount.add(sellingPrice.multiply(BigDecimal.valueOf(currentGoodQuantity)));
        }

        if (totalAmount.compareTo(nextCustomer.getAvailableMoney()) > 0) {
            returnBasket(nextCustomer, shop);
            throw new NotEnoughMoneyException("The customer does not have enough money");
        } else {
            nextCustomer.setAvailableMoney(nextCustomer.getAvailableMoney().subtract(totalAmount));
            shopService.addIncome(shop, totalAmount);
            return generateReceipt(cashDesk, receiptGoodsMap, totalAmount, shop);
        }
    }

    @Override
    public Customer getFirstCustomer(CashDesk cashDesk) {
        cashDeskValidator.validateCashDesk(cashDesk);
        if (cashDesk.isOpen()) {
            Customer nextCustomer = cashDesk.getWaitingCustomers().iterator().next();
            cashDesk.getWaitingCustomers().remove(nextCustomer);
            return nextCustomer;
        }
        return null;
    }

    @Override
    public void returnBasket(Customer currentCustomer, Shop shop) {
        cashDeskValidator.validateCustomer(currentCustomer);
        shopValidator.validateShop(shop);
        currentCustomer.getBasket().entrySet().stream()
            .filter(entry -> !(entry.getKey() != null && entry.getKey().getExpiryDate() != null &&
                entry.getKey().getExpiryDate().isBefore(LocalDate.now())))
            .forEach(entry -> shopService.returnGood(shop, entry.getKey(), entry.getValue()));
        currentCustomer.getBasket().clear();
    }

    @Override
    public Receipt generateReceipt(CashDesk cashDesk, Map<String, Map.Entry<BigDecimal, Double>> receiptGoodsMap,
                                   BigDecimal totalAmount, Shop shop) throws IOException {
        Receipt receiptToHand = new Receipt(receiptId++, shop.getName(), cashDesk.getWorkingCashier(),
            cashDesk.getCashDeskNumber(), new ReceiptValidator());

        receiptGoodsService.addGoods(receiptToHand, receiptGoodsMap);
        receiptToHand.setTotalPrice(totalAmount);

        receiptStoreInFileService.storeInFile(receiptToHand);
        receiptSerializeDeserializeService.serializeReceipt(receiptToHand);
        return receiptToHand;
    }
}