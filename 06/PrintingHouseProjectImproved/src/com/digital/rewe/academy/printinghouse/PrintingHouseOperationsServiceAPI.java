package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.math.BigDecimal;

public interface PrintingHouseOperationsServiceAPI {

    /**
     * Adds the price of a paper sheet for a specific paper size to the printing house.
     *
     * @param printingHouse the relevant printing house.
     * @param paperSize the size of the paper.
     * @param sheetPrice the price of a single sheet of the specified paper size.
     */
    void addPaperPrice(PrintingHouse printingHouse, PaperSize paperSize, BigDecimal sheetPrice);

    /**
     * Adds the print price for a specific paper size to the printing house.
     *
     * @param printingHouse the relevant printing house.
     * @param paperSize the size of the paper.
     * @param printPrice the price of printing on a single sheet of the specified paper size.
     */
    void addPrintPrice(PrintingHouse printingHouse, PaperSize paperSize, BigDecimal printPrice);
}