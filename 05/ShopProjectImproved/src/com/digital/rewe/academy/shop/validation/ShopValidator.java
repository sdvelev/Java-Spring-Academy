package com.digital.rewe.academy.shop.validation;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.EndDateBeforeStartDateException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShopValidator {
    public void validateShop(Shop shop) {
        if (shop == null) {
            throw new NullValueException("The provided shop cannot be null");
        }
    }

    public void validateShopName(String shopName) {
        if (shopName == null) {
            throw new NullValueException("The name of the shop cannot be null");
        }

        if (shopName.isBlank()) {
            throw new EmptyValueException("The name of the shop cannot be empty or blank");
        }
    }

    public void validateDaysBeforeExpiryDateDiscount(int daysBeforeExpiryDateDiscount) {
        if (daysBeforeExpiryDateDiscount < 0) {
            throw new NegativeValueException("The days before expiry date discount cannot be negative");
        }
    }

    public void validateDiscountExpiryDatePercentage(BigDecimal discountExpiryDatePercentage) {
        if (discountExpiryDatePercentage == null) {
            throw new NullValueException("The discount expiry date percentage cannot be null");
        }

        if (discountExpiryDatePercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The discount expiry date percentage cannot be negative");
        }
    }

    public void validateGoodCategory(GoodCategory goodCategory) {
        if (goodCategory == null) {
            throw new NullValueException("The provided category of good cannot be null");
        }
    }

    public void validateCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullValueException("The provided cashier cannot be null");
        }
    }

    public void validateMarkupPercentage(BigDecimal markupPercentage) {
        if (markupPercentage == null) {
            throw new NullValueException("The provided markup percentage cannot be null");
        }

        if (markupPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The provided markup percentage cannot be negative");
        }
    }

    public void validateCashDesk(CashDesk cashDesk) {
        if (cashDesk == null) {
            throw new NullValueException("The provided cash desk cannot be null");
        }
    }

    public void validateGood(Good good) {
        if (good == null) {
            throw new NullValueException("The provided good cannot be null");
        }
    }

    public void validateQuantity(double quantity) {
        if (quantity <= 0) {
            throw new NegativeValueException("The provided quantity must be positive");
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new NullValueException("The provided amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The provided amount cannot be negative");
        }
    }

    public void validateDate(LocalDate date) {
        if (date == null) {
            throw new NullValueException("The provided date cannot be null");
        }
    }

    public void validateStartDateBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new EndDateBeforeStartDateException("The provided end date is before the provided start date");
        }
    }
}