package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.tariff.TariffCalculatorAPI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PrintingHouseIncomeService implements PrintingHouseIncomeServiceAPI {

    private PrintingHouseValidator printingHouseValidator;
    private TariffCalculatorAPI tariffCalculator;

    public PrintingHouseIncomeService(PrintingHouseValidator printingHouseValidator,
                                        TariffCalculatorAPI tariffCalculator) {
        this.printingHouseValidator = printingHouseValidator;
        this.tariffCalculator = tariffCalculator;
    }

    @Override
    public BigDecimal calculateIncome(PrintingHouse printingHouse, LocalDate startDate, LocalDate endDate) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validateDateArguments(startDate, endDate);
        return printingHouse.getOrders().stream()
            .filter(order -> !order.dateOfOrder().isBefore(startDate) && !order.dateOfOrder().isAfter(endDate))
            .map(order -> tariffCalculator.calculatePrintPrice(printingHouse, order.publication())
                .multiply(BigDecimal.valueOf(order.numberOfCopies())))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }
}