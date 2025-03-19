package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.ReceiptGoodsServiceAPI;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.Map;

public class ReceiptGoodsService implements ReceiptGoodsServiceAPI {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private ReceiptValidator receiptValidator;

    public ReceiptGoodsService(ReceiptValidator receiptValidator) {
        this.receiptValidator = receiptValidator;
    }

    @Override
    public void addGood(Receipt receipt, String goodName, BigDecimal finalPrice, double quantity) {
        receiptValidator.validateGoodName(goodName);
        receiptValidator.validateTotalPrice(finalPrice);
        receiptValidator.validateQuantity(quantity);
        receipt.getGoodWithQuantityMap().put(goodName, new AbstractMap.SimpleEntry<>(finalPrice, quantity));
    }

    @Override
    public void addGoods(Receipt receipt, Map<String, Map.Entry<BigDecimal, Double>> goodWithQuantityMap) {
        if (goodWithQuantityMap == null) {
            throw new NullValueException("The provided map cannot be null");
        }
        goodWithQuantityMap.forEach((key, value) -> addGood(receipt, key,
            value.getKey().setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), value.getValue()));
    }

    @Override
    public void removeGood(Receipt receipt, String goodName) {
        receiptValidator.validateGoodName(goodName);
        receipt.getGoodWithQuantityMap().remove(goodName);
    }
}