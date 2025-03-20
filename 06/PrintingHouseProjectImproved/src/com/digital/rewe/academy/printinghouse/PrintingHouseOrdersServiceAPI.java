package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.math.BigDecimal;

public interface PrintingHouseOrdersServiceAPI {

    /**
     * Places an order for printing a specified publication.
     *
     * @return the total price of the order as BigDecimal.
     * @param printingHouse the relevant printing house.
     * @param publicationToPrint the publication to be printed.
     * @param numberOfCopies the number of copies to print.
     */
    BigDecimal makeOrder(PrintingHouse printingHouse, Publication publicationToPrint, long numberOfCopies);
}