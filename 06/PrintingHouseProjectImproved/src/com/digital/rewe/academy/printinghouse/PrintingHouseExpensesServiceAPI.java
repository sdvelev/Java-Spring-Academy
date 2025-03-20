package com.digital.rewe.academy.printinghouse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PrintingHouseExpensesServiceAPI {

    /**
     * Calculates the expenses, based on the paper needed to print the publications
     * that are ordered between the specified start and end dates.
     *
     * @return the total expenses of the printing house for the specified period as BigDecimal.
     * @param printingHouse the relevant printing house.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     */
    BigDecimal calculateExpenses(PrintingHouse printingHouse, LocalDate startDate, LocalDate endDate);
}
