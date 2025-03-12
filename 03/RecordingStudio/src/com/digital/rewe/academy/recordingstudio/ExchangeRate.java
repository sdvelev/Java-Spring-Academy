package com.digital.rewe.academy.recordingstudio;

import java.math.BigDecimal;

public enum ExchangeRate {
    FROM_BGN_TO_EUR(BigDecimal.valueOf(0.51129188)),
    FROM_EUR_TO_BGN(BigDecimal.valueOf(1.95583)),
    FROM_USD_TO_BGN(BigDecimal.valueOf(1.787)),
    FROM_GBP_TO_BGN(BigDecimal.valueOf(2.308));

    private BigDecimal exchangeRate;

    ExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
}