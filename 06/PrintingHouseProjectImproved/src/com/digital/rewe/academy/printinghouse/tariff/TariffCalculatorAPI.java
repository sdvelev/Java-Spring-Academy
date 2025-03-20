package com.digital.rewe.academy.printinghouse.tariff;

import com.digital.rewe.academy.printinghouse.PrintingHouse;
import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.math.BigDecimal;

public interface TariffCalculatorAPI {

    /**
     * Calculates the paper expenses for a given publication in a printing house.
     *
     * @param printingHouse the printing house where the publication is printed
     * @param publication the publication for which the paper expenses are calculated
     * @return the paper expenses as a BigDecimal
     */
    BigDecimal calculatePaperExpenses(PrintingHouse printingHouse, Publication publication);

    /**
     * Calculates the print price for a given publication in a printing house.
     *
     * @param printingHouse the printing house where the publication is printed
     * @param publication the publication for which the print price is calculated
     * @return the print price as a BigDecimal
     */
    BigDecimal calculatePrintPrice(PrintingHouse printingHouse, Publication publication);
}