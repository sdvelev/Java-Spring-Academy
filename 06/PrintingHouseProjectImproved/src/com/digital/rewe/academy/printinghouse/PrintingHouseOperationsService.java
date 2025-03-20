package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.math.BigDecimal;

public class PrintingHouseOperationsService implements PrintingHouseOperationsServiceAPI {

    private PrintingHouseValidator printingHouseValidator;

    public PrintingHouseOperationsService(PrintingHouseValidator printingHouseValidator) {
        this.printingHouseValidator = printingHouseValidator;
    }

    @Override
    public void addPaperPrice(PrintingHouse printingHouse, PaperSize paperSize, BigDecimal sheetPrice) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validatePaperSize(paperSize);
        printingHouseValidator.validatePrice(sheetPrice);
        printingHouse.getPaperPricePerSize().put(paperSize, sheetPrice);
    }

    @Override
    public void addPrintPrice(PrintingHouse printingHouse, PaperSize paperSize, BigDecimal printPrice) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validatePaperSize(paperSize);
        printingHouseValidator.validatePrice(printPrice);
        printingHouse.getPriceListPerSize().put(paperSize, printPrice);
    }
}