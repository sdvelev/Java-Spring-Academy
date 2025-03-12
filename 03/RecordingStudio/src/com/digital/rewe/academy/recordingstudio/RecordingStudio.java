package com.digital.rewe.academy.recordingstudio;

import com.digital.rewe.academy.recordingstudio.exception.HoursRangeException;
import com.digital.rewe.academy.recordingstudio.exception.MaxHoursExceededException;
import com.digital.rewe.academy.recordingstudio.exception.NegativeValueException;
import com.digital.rewe.academy.recordingstudio.exception.NullValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RecordingStudio implements RecordingStudioAPI, Comparable<RecordingStudio> {
    private final static BigDecimal minPriceInBGN = new BigDecimal("10");
    private final static int HOURS_PERIOD_START = 0;
    private final static int HOURS_PERIOD_END = 24;
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private static BigDecimal exchangeRateBGNtoEUR = new BigDecimal("0.51129188");

    private final long id;
    private BigDecimal priceInBGN;
    private double maxHoursPerDay;
    private final Map<LocalDate, Double> rentedHours;

    public RecordingStudio(long id, BigDecimal priceInBGN, double maxHoursPerDay) {
        this.id = id;
        setPrice(priceInBGN);
        setMaxHoursPerDay(maxHoursPerDay);
        this.rentedHours = new HashMap<>();
    }

    @Override
    public void setPrice(BigDecimal newPriceInBGNToUpdate) {
        validatePriceNotNull(newPriceInBGNToUpdate);
        validatePriceNotNegative(newPriceInBGNToUpdate);

        if (newPriceInBGNToUpdate.compareTo(minPriceInBGN) < 0) {
            newPriceInBGNToUpdate = minPriceInBGN;
        }

        this.priceInBGN = newPriceInBGNToUpdate;
    }

    @Override
    public void setMaxHoursPerDay(double maxHoursPerDay) {
        validateHoursWithinDayRange();
        this.maxHoursPerDay = maxHoursPerDay;
    }

    @Override
    public void setRentedHours(double rentedHoursToUpdate) throws MaxHoursExceededException {
        validateHourNotNegative(rentedHoursToUpdate);
        validateMaxHoursNotExceeded(rentedHoursToUpdate);
        this.rentedHours.put(LocalDate.now(), rentedHoursToUpdate);
    }

    @Override
    public void setRentedHours(LocalDate dayForRent, double rentedHoursToUpdate) throws MaxHoursExceededException {
        validateHourNotNegative(rentedHoursToUpdate);
        validateMaxHoursNotExceeded(rentedHoursToUpdate);
        this.rentedHours.put(dayForRent, rentedHoursToUpdate);
    }

    @Override
    public BigDecimal getIncomeInBGN(LocalDate dayForRent) {
        validateLocalDateNotNull(dayForRent);
        return priceInBGN.multiply(BigDecimal.valueOf(rentedHours.getOrDefault(dayForRent, 0.0)))
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeInBGN() {
        return priceInBGN.multiply(BigDecimal.valueOf(rentedHours.getOrDefault(LocalDate.now(), 0.0)))
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeInEur(LocalDate dayForRent) {
        validateLocalDateNotNull(dayForRent);
        return getIncomeInBGN(dayForRent).multiply(exchangeRateBGNtoEUR)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getIncomeInEur() {
        return getIncomeInBGN().multiply(exchangeRateBGNtoEUR)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public BigDecimal getPriceInBGN() {
        return priceInBGN;
    }

    @Override
    public double getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

    @Override
    public double getRentedHoursForDay(LocalDate dayForRent) {
        return rentedHours.getOrDefault(dayForRent, 0.0);
    }

    @Override
    public int compareTo(RecordingStudio recordingStudio, LocalDate dayForRent) {
        return this.getIncomeInBGN(dayForRent).compareTo(recordingStudio.getIncomeInBGN(dayForRent));
    }

    @Override
    public int compareTo(RecordingStudio recordingStudio) {
        return this.getIncomeInBGN(LocalDate.now()).compareTo(recordingStudio.getIncomeInBGN(LocalDate.now()));
    }

    public static BigDecimal getExchangeRateBGNtoEUR() {
        return exchangeRateBGNtoEUR;
    }

    private void validatePriceNotNegative(BigDecimal priceToValidate) {
        if (priceToValidate.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The provided price cannot be negative");
        }
    }

    private void validateHourNotNegative(double hours) {
        if (hours < 0) {
            throw new NegativeValueException("The provided hours cannot be negative");
        }
    }

    private void validateHoursWithinDayRange() {
        if (maxHoursPerDay < HOURS_PERIOD_START || maxHoursPerDay > HOURS_PERIOD_END) {
            throw new HoursRangeException("The provided maximum daily rental hours must be between 0 and 24");
        }
    }

    private void validateMaxHoursNotExceeded(double hours) throws MaxHoursExceededException {
        if (hours > maxHoursPerDay) {
            throw new MaxHoursExceededException("The provided number of hours the studio is rented for cannot exceed maximum daily rental hours");
        }
    }

    private void validatePriceNotNull(BigDecimal priceToValidate) {
        if (priceToValidate == null) {
            throw new NullValueException("The provided price cannot be null");
        }
    }

    private void validateLocalDateNotNull(LocalDate dateToValidate) {
        if (dateToValidate == null) {
            throw new NullValueException("The provided date cannot be null");
        }
    }
}