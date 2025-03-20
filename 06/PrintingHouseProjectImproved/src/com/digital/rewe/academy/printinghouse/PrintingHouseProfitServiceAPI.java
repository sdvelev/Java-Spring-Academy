package com.digital.rewe.academy.printinghouse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PrintingHouseProfitServiceAPI {

    /**
     * Calculates the profit of the printing house made between the specified start and end dates.
     *
     * @return the total profit of the printing house for the specified period as BigDecimal.
     * @param printingHouse the relevant printing house.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     */
    BigDecimal calculateProfit(PrintingHouse printingHouse, LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the net profit of the printing house made between the specified start and end dates.
     *
     * @return the total net profit of the printing house for the specified period as BigDecimal.
     * @param printingHouse the relevant printing house.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     */
    BigDecimal calculateNetProfit(PrintingHouse printingHouse, LocalDate startDate, LocalDate endDate);
}