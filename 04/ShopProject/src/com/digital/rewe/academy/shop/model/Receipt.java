package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Receipt implements Serializable {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private final long id;
    private final String shopName;
    private Cashier cashierServing;
    private final int cashDeskNumber;
    private final LocalDate dateOfPurchase;
    private final Map<String, Map.Entry<BigDecimal, Double>> goodWithQuantityMap;
    private BigDecimal totalPrice;

    public Receipt(long id, String shopName, Cashier cashierServing, int cashDeskNumber) {
        validateShopName(shopName);
        validateCashDeskNumber(cashDeskNumber);
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
        validateCashierServing(cashierServing);
        this.cashierServing = cashierServing;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        validateTotalPrice(totalPrice);
        this.totalPrice = totalPrice;
    }

    public void addGood(String goodName, BigDecimal finalPrice, double quantity) {
        validateGoodName(goodName);
        validateTotalPrice(finalPrice);
        validateQuantity(quantity);
        this.goodWithQuantityMap.put(goodName, new AbstractMap.SimpleEntry<>(finalPrice, quantity));
    }

    public void addGoods(Map<String, Map.Entry<BigDecimal, Double>> goodWithQuantityMap) {
        if (goodWithQuantityMap == null) {
            throw new NullValueException("The provided map cannot be null");
        }
        goodWithQuantityMap.forEach((key, value) -> addGood(key,
            value.getKey().setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), value.getValue()));
    }

    public void removeGood(String goodName) {
        validateGoodName(goodName);
        this.goodWithQuantityMap.remove(goodName);
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

    private void validateShopName(String shopName) {
        if (shopName == null) {
            throw new NullValueException("The provided name of the shop cannot be null");
        }

        if (shopName.isBlank()) {
            throw new EmptyValueException("The provided name of the shop cannot be empty or blank");
        }
    }

    private void validateCashierServing(Cashier cashierServing) {
        if (cashierServing == null) {
            throw new NullValueException("The cashier cannot be null");
        }
    }

    private void validateCashDeskNumber(int cashDeskNumber) {
        if (cashDeskNumber < 0) {
            throw new NegativeValueException("The cash desk number cannot be negative");
        }
    }

    private void validateGoodName(String goodName) {
        if (goodName == null) {
            throw new NullValueException("The provided name of the good cannot be null");
        }

        if (goodName.isBlank()) {
            throw new EmptyValueException("The provided name of the good cannot be empty or blank");
        }
    }

    private void validateTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null) {
            throw new NullValueException("The price argument cannot be null");
        }

        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The price cannot be negative");
        }
    }

    private void validateQuantity(double quantity) {
        if (quantity < 0) {
            throw new NegativeValueException("The quantity of the provided good cannot be negative");
        }
    }
}