package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

public abstract class Publication implements PublicationAPI {



    private final String title;
    private final LocalDate publishingDate;
    private final long numberOfPages;
    private final PaperSize paperSize;

    public Publication(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize) {
        this.title = title;
        this.publishingDate = publishingDate;
        this.numberOfPages = numberOfPages;
        this.paperSize = paperSize;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    @Override
    public long getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public PaperSize getSizeOfPaper() {
        return paperSize;
    }
}