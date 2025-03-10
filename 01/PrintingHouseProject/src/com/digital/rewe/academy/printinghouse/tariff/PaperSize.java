package com.digital.rewe.academy.printinghouse.tariff;

import java.math.BigDecimal;

public enum PaperSize {

    A1_SHEET_SIZE(BigDecimal.valueOf(1)),
    A2_SHEET_SIZE(BigDecimal.valueOf(0.6)),
    A3_SHEET_SIZE(BigDecimal.valueOf(0.3)),
    A4_SHEET_SIZE(BigDecimal.valueOf(0.02)),
    A5_SHEET_SIZE(BigDecimal.valueOf(0.01));

    private final BigDecimal price;

    PaperSize(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}