package com.digital.rewe.academy.printinghouse.tariff;

import com.digital.rewe.academy.printinghouse.PrintingHouse;
import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.math.BigDecimal;

public class TariffCalculator implements TariffCalculatorAPI {

    public PrintingHouseValidator printingHouseValidator;

    public TariffCalculator(PrintingHouseValidator printingHouseValidator) {
        this.printingHouseValidator = printingHouseValidator;
    }

    @Override
    public BigDecimal calculatePaperExpenses(PrintingHouse printingHouse, Publication publication) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validatePublication(publication);
        return printingHouse.getPaperPricePerSize().get(publication.getSizeOfPaper())
            .multiply(BigDecimal.valueOf((publication.getNumberOfPages() + 1) / 2));
    }

    @Override
    public BigDecimal calculatePrintPrice(PrintingHouse printingHouse, Publication publication) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validatePublication(publication);
        return printingHouse.getPriceListPerSize().get(publication.getSizeOfPaper())
            .multiply(BigDecimal.valueOf(publication.getNumberOfPages()));
    }
}