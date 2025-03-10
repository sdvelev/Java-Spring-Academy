package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

public abstract class Publication implements PublicationAPI {

    private String title;
    private LocalDate publishingDate;
    private long numberOfPages;
    private PaperSize paperSize;

    public Publication(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize) {
        validateConstructor(title, publishingDate, numberOfPages, paperSize);
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

    @Override
    public void setTitle(String title) {
        checkTitle(title);
        this.title = title;
    }

    @Override
    public void setPublishingDate(LocalDate publishingDate) {
        checkPublishingDate(publishingDate);
        this.publishingDate = publishingDate;
    }

    @Override
    public void setNumberOfPages(long numberOfPages) {
        checkNumberOfPages(numberOfPages);
        this.numberOfPages = numberOfPages;
    }

    @Override
    public void setPaperSize(PaperSize paperSize) {
        checkPaperSize(paperSize);
        this.paperSize = paperSize;
    }

    private void checkTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The title of the publication cannot be null or blank");
        }
    }

    private void checkPublishingDate(LocalDate publishingDate) {
        if (publishingDate == null) {
            throw new IllegalArgumentException("The publishing date of the publication cannot be null");
        }
    }

    private void checkNumberOfPages(long numberOfPages) {
        if (numberOfPages <= 0) {
            throw new IllegalArgumentException("The number of pages of the publication must be positive");
        }
    }

    private void checkPaperSize(PaperSize paperSize) {
        if (paperSize == null) {
            throw new IllegalArgumentException("The paper size of the publication cannot be null");
        }
    }

    private void validateConstructor(String title, LocalDate publishingDate, long numberOfPages, PaperSize paperSize) {
        checkTitle(title);
        checkPublishingDate(publishingDate);
        checkNumberOfPages(numberOfPages);
        checkPaperSize(paperSize);
    }
}