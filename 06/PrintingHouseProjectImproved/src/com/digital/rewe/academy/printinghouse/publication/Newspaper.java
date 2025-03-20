package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

public class Newspaper extends Publication{
    private static final String identifier = "NEWS-";
    private static int counterOfInstances = 0;

    private final long id;

    public Newspaper(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize,
                     PrintingHouseValidator printingHouseValidator) {
        super(title, publishingDate, numberOfPages, paperSize, printingHouseValidator);
        this.id=++counterOfInstances;
    }

    @Override
    public String getId() {
        return Newspaper.identifier + id;
    }
}