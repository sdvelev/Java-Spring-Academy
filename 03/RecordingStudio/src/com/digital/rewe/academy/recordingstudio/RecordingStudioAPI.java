package com.digital.rewe.academy.recordingstudio;

import com.digital.rewe.academy.recordingstudio.exception.HoursRangeException;
import com.digital.rewe.academy.recordingstudio.exception.MaxHoursExceededException;
import com.digital.rewe.academy.recordingstudio.exception.NegativeValueException;
import com.digital.rewe.academy.recordingstudio.exception.NullValueException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface that models a recording studio.
 */
public interface RecordingStudioAPI {

    /**
     * Updates the hourly rental price of the recording studio.
     *
     * @param newPriceInBGNToUpdate the new hourly rental price in BGN
     * @throws NullValueException if the provided hourly rental price is null
     * @throws NegativeValueException if the provided hourly rental price is negative
     */
    void setPrice(BigDecimal newPriceInBGNToUpdate);

    /**
     * Sets the maximum hours per day the studio can be rented.
     *
     * @param maxHoursPerDay the maximum hours per day
     * @throws HoursRangeException if the provided maximum daily rental hours are not between 0 and 24
     */
    void setMaxHoursPerDay(double maxHoursPerDay);

    /**
     * Sets the number of hours the studio is rented for the current day.
     *
     * @param rentedHoursToUpdate the rented hours to update
     * @throws MaxHoursExceededException if the rented hours exceed the maximum allowed hours per day
     * @throws NegativeValueException if the provided hours are negative
     */
    void setRentedHours(double rentedHoursToUpdate) throws MaxHoursExceededException;

    /**
     * Sets the number of hours the studio is rented for a specific day.
     *
     * @param dayForRent the day for which the hours are being set
     * @param rentedHoursToUpdate the rented hours to update
     * @throws MaxHoursExceededException if the rented hours exceed the maximum allowed hours per day
     * @throws NegativeValueException if the provided hours are negative
     * @throws NullValueException if the provided date is null
     */
    void setRentedHours(LocalDate dayForRent, double rentedHoursToUpdate) throws MaxHoursExceededException;

    /**
     * Returns the total income in BGN for a specific day.
     *
     * @param dayForRent the day for which the income is being calculated
     * @return the income in BGN
     * @throws NullValueException if the provided date is null
     */
    BigDecimal getIncomeInBGN(LocalDate dayForRent);

    /**
     * Returns the total income in BGN for the current day.
     *
     * @return the income in BGN
     */
    BigDecimal getIncomeInBGN();

    /**
     * Returns the total income in EUR for a specific day.
     *
     * @param dayForRent the day for which the income is being calculated
     * @return the income in EUR
     * @throws NullValueException if the provided date is null
     */
    BigDecimal getIncomeInEur(LocalDate dayForRent);

    /**
     * Returns the total income in EUR for the current day.
     *
     * @return the income in EUR
     */
    BigDecimal getIncomeInEur();

    /**
     * Gets the ID of the recording studio.
     *
     * @return the ID of the recording studio
     */
    long getId();

    /**
     * Gets the hourly rental price in BGN of the recording studio.
     *
     * @return the price in BGN
     */
    BigDecimal getPriceInBGN();

    /**
     * Gets the maximum hours the studio can be rented per day.
     *
     * @return the maximum hours per day
     */
    double getMaxHoursPerDay();

    /**
     * Gets the rented hours for the current day.
     *
     * @return the rented hours for the current day
     */
    double getRentedHours();

    /**
     * Gets the rented hours for a specific day.
     *
     * @param dayForRent the day for which the rented hours are being retrieved
     * @return the rented hours for the specified day
     * @throws NullValueException if the provided date is null
     */
    double getRentedHoursForDay(LocalDate dayForRent);

    /**
     * Compares the income of this recording studio with another for a specific day.
     *
     * @param recordingStudio the other recording studio to compare with
     * @param dayForRent the day for which the income is being compared
     * @return a negative integer, zero, or a positive integer as this recording studio's income is less than, equal to, or greater than the other recording studio's income
     * @throws NullValueException if the provided recording studio is null
     * @throws NullValueException if the provided date is null
     */
    int compareTo(RecordingStudio recordingStudio, LocalDate dayForRent);
}