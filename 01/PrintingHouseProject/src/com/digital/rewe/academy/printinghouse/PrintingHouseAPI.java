package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface, representing the API for a printing house.
 */
public interface PrintingHouseAPI {

   /**
     * Places an order for printing a specified publication.
     *
     * @return the total price of the order as BigDecimal.
     * @param publicationToPrint the publication to be printed.
     * @param numberOfCopies the number of copies to print.
     * @throws IllegalArgumentException if the publication to print is null,
     *                                  or if the number of copies is less than one.
     */
    BigDecimal makeOrder(Publication publicationToPrint, long numberOfCopies);

    /**
     * Calculates the expenses, based on the paper needed to print the publications
     * that are ordered between the specified start and end dates.
     *
     * @return the total expenses of the printing house for the specified period as BigDecimal.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     * @throws IllegalArgumentException if start date or end date is null,
     *                                  or if the start date is after the end date.
     */
    BigDecimal calculateExpenses(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the income of the printing house, based on the printed publications
     * that are ordered between the specified start and end dates.
     *
     * @return the total income of the printing house for the specified period as BigDecimal.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     * @throws IllegalArgumentException if start date or end date is null,
     *                                  or if the start date is after the end date.
     */
    BigDecimal calculateIncome(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the profit of the printing house made between the specified start and end dates.
     *
     * @return the total profit of the printing house for the specified period as BigDecimal.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     * @throws IllegalArgumentException if start date or end date is null,
     *                                  or if the start date is after the end date.
     */
    BigDecimal calculateProfit(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the net profit of the printing house made between the specified start and end dates.
     *
     * @return the total net profit of the printing house for the specified period as BigDecimal.
     * @param startDate the start date for the calculation period.
     * @param endDate the end date for the calculation period (included).
     * @throws IllegalArgumentException if start date or end date is null,
     *                                  or if the start date is after the end date.
     */
    BigDecimal calculateNetProfit(LocalDate startDate, LocalDate endDate);
}