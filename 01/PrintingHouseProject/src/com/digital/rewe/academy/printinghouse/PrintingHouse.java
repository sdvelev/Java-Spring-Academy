package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.order.Order;
import com.digital.rewe.academy.printinghouse.publication.Book;
import com.digital.rewe.academy.printinghouse.publication.Newspaper;
import com.digital.rewe.academy.printinghouse.publication.Poster;
import com.digital.rewe.academy.printinghouse.publication.Publication;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        return TariffCalculator.calculatePrintPrice(publicationToPrint).multiply(BigDecimal.valueOf(numberOfCopies))
                                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateExpenses(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        for (Order currentOrder : orders) {
            if (dateWithinRange(currentOrder.dateOfOrder(), startDate, endDate)) {
                totalExpenses = totalExpenses.add(TariffCalculator
                    .calculatePaperExpenses(currentOrder.publication())
                    .multiply(BigDecimal.valueOf(currentOrder.numberOfCopies())));
            }
        }

        return totalExpenses.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateIncome(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Order currentOrder : orders) {
            if (dateWithinRange(currentOrder.dateOfOrder(), startDate, endDate)) {
                totalIncome = totalIncome.add(TariffCalculator
                    .calculatePrintPrice(currentOrder.publication())
                    .multiply(BigDecimal.valueOf(currentOrder.numberOfCopies())));
            }
        }

        return totalIncome.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateProfit(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        return calculateIncome(startDate, endDate).subtract(calculateExpenses(startDate, endDate))
                                                  .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateNetProfit(LocalDate startDate, LocalDate endDate) {
        checkDateArguments(startDate, endDate);
        return calculateProfit(startDate, endDate).multiply(BigDecimal.valueOf(1 - PROFIT_TAX_PERCENTAGE))
                                                  .setScale(2, RoundingMode.HALF_UP);
    }

    public long getId() {
        return id;
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
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("The start date and the end date cannot be null");
        }

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
            throw new IllegalArgumentException("The argument for number of copies must be positive");
        }
    }

    public static void main(String[] args) {
        Publication frankenstein = new Book("Frankenstein; or, The Modern Prometheus",
                                            LocalDate.of(1818, 1, 1), 258,
                                            PaperSize.A4_SHEET_SIZE);

        Publication newYorkTimes = new Newspaper("The New York Times /2025-03-10/", LocalDate.now(),
                                    20, PaperSize.A3_SHEET_SIZE);

        Publication reweAcademy = new Poster("REWE digital Academy 2025: Poster",
                                            LocalDate.of(2024, 12, 1),
                               2, PaperSize.A5_SHEET_SIZE);

        PrintingHouse printingHouse = new PrintingHouse(1, "My Printing House");

        BigDecimal orderFrankenstein = printingHouse.makeOrder(frankenstein, 6);
        System.out.println("Order Frankenstein Cost: " + orderFrankenstein);

        System.out.println("Expenses: " + printingHouse.calculateExpenses(LocalDate.now(), LocalDate.now()));
        System.out.println("Income: " + printingHouse.calculateIncome(LocalDate.now(), LocalDate.now()));
        System.out.println("Profit: " + printingHouse.calculateProfit(LocalDate.now(), LocalDate.now()));
        System.out.println("Net Profit: " + printingHouse.calculateNetProfit(LocalDate.now(), LocalDate.now()));

        BigDecimal orderNewYorkTimes = printingHouse.makeOrder(newYorkTimes, 10);
        System.out.println("Order NYT Cost: " + orderNewYorkTimes);

        System.out.println("Expenses: " + printingHouse.calculateExpenses(LocalDate.of(2025, 3, 11),
                                                                          LocalDate.of(2025, 3, 12)));
        System.out.println("Income: " + printingHouse.calculateIncome(LocalDate.of(2025, 3, 11),
                                                                      LocalDate.of(2025, 3, 12)));
        System.out.println("Profit: " + printingHouse.calculateProfit(LocalDate.of(2025, 3, 11),
                                                                      LocalDate.of(2025, 3, 12)));
        System.out.println("Net Profit: " + printingHouse.calculateNetProfit(LocalDate.of(2025, 3, 11),
                                                                             LocalDate.of(2025, 3, 12)));

        BigDecimal orderReweAcademy = printingHouse.makeOrder(reweAcademy, 600);
        System.out.println("Order REWE digital Academy: " + orderReweAcademy);

        System.out.println("Expenses: " + printingHouse.calculateExpenses(LocalDate.of(2024, 1, 1),
                                                                          LocalDate.of(2025, 12, 1)));
        System.out.println("Income: " + printingHouse.calculateIncome(LocalDate.of(2024, 1, 1),
                                                                      LocalDate.of(2025, 12, 1)));
        System.out.println("Profit: " + printingHouse.calculateProfit(LocalDate.of(2024, 1, 1),
                                                                      LocalDate.of(2025, 12, 1)));
        System.out.println("Net Profit: " + printingHouse.calculateNetProfit(LocalDate.of(2024, 1, 1),
                                                                             LocalDate.of(2025, 12, 1)));

        System.out.println("The ID of " + frankenstein.getTitle() + " is " + frankenstein.getId());
        System.out.println("The ID of " + newYorkTimes.getTitle() + " is " + newYorkTimes.getId());
        System.out.println("The ID of " + reweAcademy.getTitle() + " is " + reweAcademy.getId());

        Publication andThenThereWereNone = new Book("And Then There Were None /Ten Little Niggers/",
        LocalDate.of(1939, 11, 6), 272,
            PaperSize.A4_SHEET_SIZE);

        Publication theTimes = new Newspaper("The Times /2025-03-10/", LocalDate.now(),
            20, PaperSize.A3_SHEET_SIZE);

        Publication reweAcademyPrevious = new Poster("REWE digital Academy 2023: Poster",
            LocalDate.of(2022, 12, 1),
            2, PaperSize.A5_SHEET_SIZE);

        System.out.println("The ID of " + andThenThereWereNone.getTitle() + " is " + andThenThereWereNone.getId());
        System.out.println("The ID of " + theTimes.getTitle() + " is " + theTimes.getId());
        System.out.println("The ID of " + reweAcademyPrevious.getTitle() + " is " + reweAcademyPrevious.getId());
    }
}