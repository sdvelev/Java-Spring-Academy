package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

public class Poster extends Publication {
    private static final String identifier = "POST-";
    private static int counterOfInstances = 0;

    private final long id;

    public Poster(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize,
                  PrintingHouseValidator printingHouseValidator) {
        super(title, publishingDate, numberOfPages, paperSize, printingHouseValidator);
        this.id=++counterOfInstances;
    }

    @Override
    public String getId() {
        return Poster.identifier + id;
    }
}