package com.digital.rewe.academy.printinghouse.tariff;

import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.math.BigDecimal;

public class TariffCalculator {

    public static BigDecimal calculatePaperExpenses(Publication publication) {

        return publication.getSizeOfPaper().getPrice().multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
    }

    public static BigDecimal calculatePrintPrice(Publication publication) {

        return switch(publication.getSizeOfPaper()) {
            case A1_SHEET_SIZE -> PriceList.A1_PRINT_PAGE_PRICE.getPrice().multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
            case A2_SHEET_SIZE -> PriceList.A2_PRINT_PAGE_PRICE.getPrice().multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
            case A3_SHEET_SIZE -> PriceList.A3_PRINT_PAGE_PRICE.getPrice().multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
            case A4_SHEET_SIZE -> PriceList.A4_PRINT_PAGE_PRICE.getPrice().multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
            case A5_SHEET_SIZE -> PriceList.A5_PRINT_PAGE_PRICE.getPrice().multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
        };
    }
}