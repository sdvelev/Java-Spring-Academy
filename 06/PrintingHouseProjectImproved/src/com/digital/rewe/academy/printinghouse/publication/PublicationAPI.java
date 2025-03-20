package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.exception.EmptyValueException;
import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.time.LocalDate;

/**
 * Interface, representing the API for a publication.
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
     * Retrieves the publishing date of the publication.
     *
     * @return the publishing date of the publication.
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

    /**
     * Sets the title of the publication.
     *
     * @param title the new title of the publication.
     * @throws NullValueException if the title is null.
     * @throws EmptyValueException if the title is empty or blank.
     */
    void setTitle(String title);

    /**
     * Sets the publishing date of the publication.
     *
     * @param publishingDate the new publishing date of the publication.
     * @throws NullValueException if the publishing date is null.
     */
    void setPublishingDate(LocalDate publishingDate);

    /**
     * Sets the number of pages of the publication.
     *
     * @param numberOfPages the new number of pages of the publication.
     * @throws NegativeValueException if the number of pages of the publication is not positive.
     */
    void setNumberOfPages(long numberOfPages);

    /**
     * Sets the size of the paper used for the publication.
     *
     * @param paperSize the new size of the paper used for the publication.
     * @throws NullValueException if the paper size of the publication is null.
     */
    void setPaperSize(PaperSize paperSize);
}