package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.order.Order;
import com.digital.rewe.academy.printinghouse.publication.Publication;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PrintingHouse implements PrintingHouseAPI {

    private static final double PROFIT_TAX_PERCENTAGE = 0.1;

    private final long id;
    private String houseName;
    private final Set<Order> orders;
    private long counterOfOrderInstances;

    public PrintingHouse(long id, String houseName) {
        this.id = id;
        this.houseName = houseName;
        this.orders = new HashSet<>();
        this.counterOfOrderInstances = 0;
    }

    public PrintingHouse(long id, String houseName, Set<Order> orders) {
        this.id = id;
        this.houseName = houseName;
        this.orders = orders;
        this.counterOfOrderInstances = 0;
    }

    @Override
    public BigDecimal makeOrder(Publication publicationToPrint, long numberOfCopies) {
        checkOrderPublicationIsNotNull(publicationToPrint);
        checkNumberOfCopiesIsPositive(numberOfCopies);
        this.orders.add(new Order(++counterOfOrderInstances, publicationToPrint, numberOfCopies, LocalDate.now()));
        return TariffCalculator.calculatePrintPrice(publicationToPrint);
    }

    //TODO Delete this method after testing
    public BigDecimal makeOrder(Publication publicationToPrint, long numberOfCopies, LocalDate date) {
        checkOrderPublicationIsNotNull(publicationToPrint);
        checkNumberOfCopiesIsPositive(numberOfCopies);
        this.orders.add(new Order(++counterOfOrderInstances, publicationToPrint, numberOfCopies, date));
        return TariffCalculator.calculatePrintPrice(publicationToPrint);
    }

    @Override
    public BigDecimal calculateExpenses(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        for (Order currentOrder : orders) {
            if (dateWithinRange(currentOrder.dateOfOrder(), startDate, endDate)) {
                totalExpenses = totalExpenses.add(TariffCalculator
                    .calculatePaperExpenses(currentOrder.publication()).
                    multiply(BigDecimal.valueOf(currentOrder.numberOfCopies())));
            }
        }

        return totalExpenses;
    }

    @Override
    public BigDecimal calculateIncome(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Order currentOrder : orders) {
            if (dateWithinRange(currentOrder.dateOfOrder(), startDate, endDate)) {
                totalIncome= totalIncome.add(TariffCalculator
                    .calculatePrintPrice(currentOrder.publication()).
                    multiply(BigDecimal.valueOf(currentOrder.numberOfCopies())));
            }
        }

        return totalIncome;
    }

    @Override
    public BigDecimal calculateProfit(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        return calculateIncome(startDate, endDate).subtract(calculateExpenses(startDate, endDate));
    }

    @Override
    public BigDecimal calculateNetProfit(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        return calculateProfit(startDate, endDate).multiply(BigDecimal.valueOf(1 - PROFIT_TAX_PERCENTAGE));
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    private boolean dateWithinRange(LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
        return !(currentDate.isBefore(startDate) || currentDate.isAfter(endDate));
    }

    private void checkDateArguments(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The start date argument cannot be after end date");
        }
    }

    private void checkOrderPublicationIsNotNull(Publication publicationToPrint) {
        if (publicationToPrint == null) {
            throw new IllegalArgumentException("The publication argument cannot be null");
        }
    }

    private void checkNumberOfCopiesIsPositive(long numberOfCopies) {
        if (numberOfCopies < 1) {
            throw new IllegalArgumentException("The number of copies argument must be positive");
        }
    }
}