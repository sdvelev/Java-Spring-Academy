package com.digital.rewe.academy.shop.api;

import com.digital.rewe.academy.shop.exception.EndDateBeforeStartDateException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface for financial operations related to income, costs, profits.
 */
public interface ShopFinancialServiceAPI {

 /**
  * Gets the total costs for a specific day.
  *
  * @param shop the shop for which to get the costs
  * @param targetDate the date for which to get the costs
  * @return the total costs for the specified day
  * @throws NullValueException if the provided date is null
  */
 BigDecimal getCostsForDay(Shop shop, LocalDate targetDate);

 /**
  * Gets the total costs for a specific period.
  *
  * @param shop the shop for which to get the costs
  * @param startDate the start date of the period
  * @param endDate the end date of the period
  * @return the total costs for the specified period
  * @throws NullValueException if any of the provided dates are null
  * @throws EndDateBeforeStartDateException if the end date is before the start date
  */
 BigDecimal getCostsForPeriod(Shop shop, LocalDate startDate, LocalDate endDate);

 /**
  * Gets the total income for a specific day.
  *
  * @param shop the shop for which to get the income
  * @param targetDate the date for which to get the income
  * @return the total income for the specified day
  * @throws NullValueException if the provided date is null
  */
 BigDecimal getIncomeForDay(Shop shop, LocalDate targetDate);

 /**
  * Gets the total income for a specific period.
  *
  * @param shop the shop for which to get the income
  * @param startDate the start date of the period
  * @param endDate the end date of the period
  * @return the total income for the specified period
  * @throws NullValueException if any of the provided dates are null
  * @throws EndDateBeforeStartDateException if the end date is before the start date
  */
 BigDecimal getIncomeForPeriod(Shop shop, LocalDate startDate, LocalDate endDate);

 /**
  * Gets the profit for a specific day.
  *
  * @param shop the shop for which to get the profit
  * @param targetDate the date for which to get the profit
  * @return the profit for the specified day
  * @throws NullValueException if the provided date is null
  */
 BigDecimal getProfitForDay(Shop shop, LocalDate targetDate);

 /**
  * Gets the profit for a specific period.
  *
  * @param shop the shop for which to get the profit
  * @param startDate the start date of the period
  * @param endDate the end date of the period
  * @return the profit for the specified period
  * @throws NullValueException if any of the provided dates are null
  * @throws EndDateBeforeStartDateException if the end date is before the start date
  */
 BigDecimal getProfitForPeriod(Shop shop, LocalDate startDate, LocalDate endDate);

 /**
  * Checks if the given date is the end of the month.
  *
  * @param date the date to check
  * @return true if the date is the end of the month, false otherwise
  * @throws NullValueException if the provided date is null
  */
 boolean shouldAddSalary(LocalDate date);

 /**
  * Gets the total salaries of all cashiers in the shop.
  *
  * @param shop the shop for which to get the total salaries
  * @return the total salaries of all cashiers
  * @throws NullValueException if the provided shop is null
  */
 BigDecimal getTotalSalaries(Shop shop);
}