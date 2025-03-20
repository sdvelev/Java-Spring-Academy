package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.order.Order;
import com.digital.rewe.academy.printinghouse.publication.Publication;
import com.digital.rewe.academy.printinghouse.tariff.TariffCalculatorAPI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PrintingHouseOrdersService implements PrintingHouseOrdersServiceAPI {
    private PrintingHouseValidator printingHouseValidator;
    private TariffCalculatorAPI tariffCalculator;

    public PrintingHouseOrdersService(PrintingHouseValidator printingHouseValidator,
                                          TariffCalculatorAPI tariffCalculator) {
        this.printingHouseValidator = printingHouseValidator;
        this.tariffCalculator = tariffCalculator;
    }

    @Override
    public BigDecimal makeOrder(PrintingHouse printingHouse, Publication publicationToPrint, long numberOfCopies) {
        printingHouseValidator.validatePrintingHouse(printingHouse);
        printingHouseValidator.validatePublication(publicationToPrint);
        printingHouseValidator.validateNumberOfCopies(numberOfCopies);
        printingHouse.setCounterOfOrderInstances(printingHouse.getCounterOfOrderInstances() + 1);
        printingHouse.getOrders().add(new Order(printingHouse.getCounterOfOrderInstances(), publicationToPrint,
            numberOfCopies, LocalDate.now()));

        return tariffCalculator.calculatePrintPrice(printingHouse, publicationToPrint).multiply(BigDecimal.valueOf(numberOfCopies))
            .setScale(2, RoundingMode.HALF_UP);
    }
}