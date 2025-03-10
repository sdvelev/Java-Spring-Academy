package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

public class Book extends Publication {
    private static final String identifier = "BOOK-";
    private static int counterOfInstances = 0;

    private final int id;

    public Book(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize) {
        super(title, publishingDate, numberOfPages, paperSize);
        this.id=++counterOfInstances;
    }

    @Override
    public String getId() {
        return Book.identifier + id;
    }
}