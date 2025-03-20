package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

public abstract class Publication implements PublicationAPI {

    private String title;
    private LocalDate publishingDate;
    private long numberOfPages;
    private PaperSize paperSize;

    private PrintingHouseValidator printingHouseValidator;

    public Publication(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize,
                       PrintingHouseValidator printingHouseValidator) {
        this.printingHouseValidator = printingHouseValidator;
        this.setTitle(title);
        this.setPublishingDate(publishingDate);
        this.setNumberOfPages(numberOfPages);
        this.setPaperSize(paperSize);
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

    @Override
    public void setTitle(String title) {
        printingHouseValidator.validateTitle(title);
        this.title = title;
    }

    @Override
    public void setPublishingDate(LocalDate publishingDate) {
        printingHouseValidator.validatePublishingDate(publishingDate);
        this.publishingDate = publishingDate;
    }

    @Override
    public void setNumberOfPages(long numberOfPages) {
        printingHouseValidator.validateNumberOfPages(numberOfPages);
        this.numberOfPages = numberOfPages;
    }

    @Override
    public void setPaperSize(PaperSize paperSize) {
        printingHouseValidator.validatePaperSize(paperSize);
        this.paperSize = paperSize;
    }
}