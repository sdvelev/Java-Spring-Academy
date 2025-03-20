package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.exception.EmptyValueException;
import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublicationIntegrationTest {

    private PrintingHouseValidator printingHouseValidator;
    private Publication publication;

    @BeforeEach
    void setUp() {
        printingHouseValidator = new PrintingHouseValidator();
        publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidator);
    }

    @Test
    void testSetTitlePositiveCase() {
        String newTitle = "Frankenstein";
        publication.setTitle(newTitle);

        assertEquals(newTitle, publication.getTitle(),
            "The title of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetTitleWithNullValue() {
        assertThrows(NullValueException.class, () -> publication.setTitle(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetTitleWithBlankValue() {
        assertThrows(EmptyValueException.class, () -> publication.setTitle("         "),
            "EmptyValueException expected to be thrown when blank value is given");
    }

    @Test
    void testSetPublishingDatePositiveCase() {
        LocalDate newPublishingDate = LocalDate.of(1818, 2, 1);
        publication.setPublishingDate(newPublishingDate);

        assertEquals(newPublishingDate, publication.getPublishingDate(),
            "The publishing date of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetPublishingDateWithNullValue() {
        assertThrows(NullValueException.class, () -> publication.setPublishingDate(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetNumberOfPagesPositiveCase() {
        long newNumberOfPages = 260L;
        publication.setNumberOfPages(newNumberOfPages);

        assertEquals(newNumberOfPages, publication.getNumberOfPages(),
            "The number of pages of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetNumberOfPagesWithNegativeValue() {
        assertThrows(NegativeValueException.class, () -> publication.setNumberOfPages(-258L),
            "NegativeValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetPaperSizePositiveCase() {
        PaperSize newPaperSize = PaperSize.A5_SHEET_SIZE;
        publication.setPaperSize(newPaperSize);

        assertEquals(newPaperSize, publication.getSizeOfPaper(),
            "The size of paper of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetPaperSizeWithNullValue() {
        assertThrows(NullValueException.class, () -> publication.setPaperSize(null),
            "NullValueException expected to be thrown when null value is given");
    }
}