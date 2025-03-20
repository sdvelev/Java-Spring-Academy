package com.digital.rewe.academy.printinghouse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PrintingHouseProfitService implements PrintingHouseProfitServiceAPI {
    private static double PROFIT_TAX_PERCENTAGE = 0.1;


    private PrintingHouseValidator printingHouseValidator;
    private PrintingHouseExpensesServiceAPI printingHouseExpensesService;
    private PrintingHouseIncomeServiceAPI printingHouseIncomeService;

    public PrintingHouseProfitService(PrintingHouseValidator printingHouseValidator,
                                      PrintingHouseExpensesServiceAPI printingHouseExpensesService,
                                      PrintingHouseIncomeServiceAPI printingHouseIncomeService) {
        this.printingHouseValidator = printingHouseValidator;
        this.printingHouseExpensesService = printingHouseExpensesService;
        this.printingHouseIncomeService = printingHouseIncomeService;
    }

    @Override
    public BigDecimal calculateProfit(PrintingHouse printingHouse, LocalDate startDate, LocalDate endDate) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validateDateArguments(startDate, endDate);
        return printingHouseIncomeService.calculateIncome(printingHouse, startDate, endDate)
            .subtract(printingHouseExpensesService.calculateExpenses(printingHouse, startDate, endDate))
            .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateNetProfit(PrintingHouse printingHouse, LocalDate startDate, LocalDate endDate) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validateDateArguments(startDate, endDate);
        return calculateProfit(printingHouse, startDate, endDate).multiply(BigDecimal.valueOf(1 - PROFIT_TAX_PERCENTAGE))
            .setScale(2, RoundingMode.HALF_UP);
    }
}