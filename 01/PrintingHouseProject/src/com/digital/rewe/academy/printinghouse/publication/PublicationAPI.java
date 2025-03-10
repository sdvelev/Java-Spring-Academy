package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

/**
 * Interface representing the API for a publication.
 */
public interface PublicationAPI {

    /**
     * Retrieves the unique identifier of the publication.
     *
     * @return the unique identifier of the publication as a String.
     * It is String with prefix the first four letters (all-caps) of the publication type,
     * a dash and a sequential number counting the number of created publication instances of the respective type:
     * e.g. "BOOK-3", "POST-7", "NEWS-6". Counting starts from 1 and is done separately for the different types.
     */
    String getId();

    /**
     * Retrieves the title of the publication.
     *
     * @return the title of the publication as a String.
     */
    String getTitle();

    /**
     * Retrieves the publication date of the publication.
     *
     * @return the publication date of the publication.
     */
    LocalDate getPublishingDate();

    /**
     * Retrieves the number of pages of the publication.
     *
     * @return the number of pages of the publication.
     */
    long getNumberOfPages();

    /**
     * Retrieves the size of the paper used for the publication.
     *
     * @return the size of the paper used for the publication.
     */
    PaperSize getSizeOfPaper();
}