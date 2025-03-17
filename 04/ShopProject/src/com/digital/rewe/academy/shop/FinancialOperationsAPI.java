package com.digital.rewe.academy.shop;

import com.digital.rewe.academy.shop.exception.EndDateBeforeStartDateException;
import com.digital.rewe.academy.shop.exception.NullValueException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface for financial operations related to income, costs, profits.
 */
public interface FinancialOperationsAPI {

     /**
     * Gets the total costs for a specific day.
     *
     * @param targetDate the date for which to get the costs
     * @return the total costs for the specified day
     * @throws NullValueException if the provided date is null
     */
    BigDecimal getCostsForDay(LocalDate targetDate);

     /**
      * Gets the total costs for a specific period.
      *
      * @param startDate the start date of the period
      * @param endDate the end date of the period
      * @return the total costs for the specified period
      * @throws NullValueException if any of the provided dates are null
      * @throws EndDateBeforeStartDateException if the end date is before the start date
      */
     BigDecimal getCostsForPeriod(LocalDate startDate, LocalDate endDate);

     /**
      * Gets the total income for a specific day.
      *
      * @param targetDate the date for which to get the income
      * @return the total income for the specified day
      * @throws NullValueException if the provided date is null
      */
     BigDecimal getIncomeForDay(LocalDate targetDate);

     /**
      * Gets the total income for a specific period.
      *
      * @param startDate the start date of the period
      * @param endDate the end date of the period
      * @return the total income for the specified period
      * @throws NullValueException if any of the provided dates are null
      * @throws EndDateBeforeStartDateException if the end date is before the start date
      */
     BigDecimal getIncomeForPeriod(LocalDate startDate, LocalDate endDate);

     /**
      * Gets the profit for a specific day.
      *
      * @param targetDate the date for which to get the profit
      * @return the profit for the specified day
      * @throws NullValueException if the provided date is null
      */
     BigDecimal getProfitForDay(LocalDate targetDate);

     /**
      * Gets the profit for a specific period.
      *
      * @param startDate the start date of the period
      * @param endDate the end date of the period
      * @return the profit for the specified period
      * @throws NullValueException if any of the provided dates are null
      * @throws EndDateBeforeStartDateException if the end date is before the start date
      */
     BigDecimal getProfitForPeriod(LocalDate startDate, LocalDate endDate);
}