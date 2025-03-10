package com.digital.rewe.academy.printinghouse.tariff;

import java.math.BigDecimal;

public enum PriceList {

    A1_PRINT_PAGE_PRICE(BigDecimal.valueOf(1.5)),
    A2_PRINT_PAGE_PRICE(BigDecimal.valueOf(1.2)),
    A3_PRINT_PAGE_PRICE(BigDecimal.valueOf(1)),
    A4_PRINT_PAGE_PRICE(BigDecimal.valueOf(0.35)),
    A5_PRINT_PAGE_PRICE(BigDecimal.valueOf(0.2));

    private final BigDecimal price;

    PriceList(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}