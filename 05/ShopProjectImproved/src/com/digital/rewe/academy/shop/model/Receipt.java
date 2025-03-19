package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.validation.ReceiptValidator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Receipt implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private transient ReceiptValidator receiptValidator;

    private final long id;
    private final String shopName;
    private Cashier cashierServing;
    private final int cashDeskNumber;
    private final LocalDate dateOfPurchase;
    private final Map<String, Map.Entry<BigDecimal, Double>> goodWithQuantityMap;
    private BigDecimal totalPrice;

    public Receipt(long id, String shopName, Cashier cashierServing, int cashDeskNumber, ReceiptValidator receiptValidator) {
        this.receiptValidator = receiptValidator;
        receiptValidator.validateShopName(shopName);
        receiptValidator.validateCashDeskNumber(cashDeskNumber);
        this.id = id;
        this.shopName = shopName;
        this.setCashierServing(cashierServing);
        this.cashDeskNumber = cashDeskNumber;
        this.dateOfPurchase = LocalDate.now();
        this.goodWithQuantityMap = new LinkedHashMap<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public String getShopName() {
        return shopName;
    }

    public int getCashDeskNumber() {
        return cashDeskNumber;
    }

    public Cashier getCashierServing() {
        return cashierServing;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public Map<String, Map.Entry<BigDecimal, Double>> getGoodWithQuantityMap() {
        return goodWithQuantityMap;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice.setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    public void setCashierServing(Cashier cashierServing) {
        receiptValidator.validateCashierServing(cashierServing);
        this.cashierServing = cashierServing;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        receiptValidator.validateTotalPrice(totalPrice);
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return id == receipt.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + id +
            ", shopName='" + shopName + '\'' +
            ", cashierServing=" + cashierServing +
            ", cashDeskNumber=" + cashDeskNumber +
            ", dateOfPurchase=" + dateOfPurchase +
            ", goodWithQuantityMap=" + goodWithQuantityMap +
            ", totalPrice=" + getTotalPrice() +
            '}';
    }
}