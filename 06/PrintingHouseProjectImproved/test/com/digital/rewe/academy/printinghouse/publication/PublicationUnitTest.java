package com.digital.rewe.academy.printinghouse.publication;

import com.digital.rewe.academy.printinghouse.PrintingHouseValidator;
import com.digital.rewe.academy.printinghouse.exception.EmptyValueException;
import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublicationUnitTest {


    @Mock
    private PrintingHouseValidator printingHouseValidatorMock = Mockito.mock(PrintingHouseValidator.class);

    private Publication publication;

    @BeforeEach
    void setUp() {
        publication = new Book("Frankenstein; or, The Modern Prometheus",
            LocalDate.of(1818, 1, 1), 258,
            PaperSize.A4_SHEET_SIZE, printingHouseValidatorMock);
    }

    @Test
    void testSetTitlePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(printingHouseValidatorMock).validateTitle(Mockito.anyString());
        String newTitle = "Frankenstein";
        publication.setTitle(newTitle);
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("title"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualTitle = (String) field.get(publication);
        Mockito.verify(printingHouseValidatorMock, Mockito.times(2)).validateTitle(Mockito.anyString());
        assertEquals(newTitle, actualTitle, "The title of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetTitleWithBlankValue() throws IllegalAccessException {
        Mockito.doThrow(EmptyValueException.class).when(printingHouseValidatorMock).validateTitle(Mockito.anyString());
        String newTitle = "              ";
        assertThrows(EmptyValueException.class, () -> publication.setTitle(newTitle),
            "EmptyValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("title"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        String actualTitle = (String) field.get(publication);
        assertEquals("Frankenstein; or, The Modern Prometheus", actualTitle,
            "The title of the publication is not expected to change but it has");
    }

    @Test
    void testSetPublishingDatePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePublishingDate(Mockito.any(LocalDate.class));
        LocalDate newDate = LocalDate.of(1818, 1, 1);
        publication.setPublishingDate(newDate);
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("publishingDate"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LocalDate actualPublishingDate = (LocalDate) field.get(publication);
        Mockito.verify(printingHouseValidatorMock, Mockito.times(2)).validatePublishingDate(Mockito.any(LocalDate.class));
        assertEquals(newDate, actualPublishingDate,
            "The publishing date of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetPublishingDateWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePublishingDate(Mockito.any());
        LocalDate newDate = null;
        assertThrows(NullValueException.class, () -> publication.setPublishingDate(newDate),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("publishingDate"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        LocalDate actualDate = (LocalDate) field.get(publication);
        assertEquals(LocalDate.of(1818, 1, 1), actualDate,
            "The publishing date of the publication is not expected to change but it has");
    }

    @Test
    void testSetNumberOfPagesPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(printingHouseValidatorMock).validateNumberOfPages(Mockito.any(Long.class));
        long newNumberOfPages = 260;
        publication.setNumberOfPages(newNumberOfPages);
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("numberOfPages"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        long actualNumberOfPages = (long) field.get(publication);
        Mockito.verify(printingHouseValidatorMock, Mockito.times(2)).validateNumberOfPages(Mockito.any(Long.class));
        assertEquals(newNumberOfPages, actualNumberOfPages,
            "The number of pages of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetNumberOfPagesWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(printingHouseValidatorMock)
            .validateNumberOfPages(Mockito.any(Long.class));
        long newNumberOfPages = -260;
        assertThrows(NegativeValueException.class, () -> publication.setNumberOfPages(newNumberOfPages),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("numberOfPages"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        long actualNumberOfPages = (long) field.get(publication);
        assertEquals(258, actualNumberOfPages,
            "The number of pages of the publication is not expected to change but it has");
    }

    @Test
    void testSetPaperSizePositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        PaperSize newPaperSize = PaperSize.A5_SHEET_SIZE;
        publication.setPaperSize(newPaperSize);
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("paperSize"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        PaperSize actualPaperSize = (PaperSize) field.get(publication);
        Mockito.verify(printingHouseValidatorMock, Mockito.times(2)).validatePaperSize(Mockito.any());
        assertEquals(newPaperSize, actualPaperSize,
            "The paper size of the publication is expected to change but remains the old one");
    }

    @Test
    void testSetPaperSizeWithNullValue() throws IllegalAccessException {
        Mockito.doThrow(NullValueException.class).when(printingHouseValidatorMock).validatePaperSize(Mockito.any());
        PaperSize newPaperSize = null;
        assertThrows(NullValueException.class, () -> publication.setPaperSize(newPaperSize),
            "NullValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Publication.class, f -> f.getName().equals("paperSize"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        PaperSize actualPaperSize = (PaperSize) field.get(publication);
        assertEquals(PaperSize.A4_SHEET_SIZE, actualPaperSize,
            "The paper size of the publication is not expected to change but it has");
    }
}