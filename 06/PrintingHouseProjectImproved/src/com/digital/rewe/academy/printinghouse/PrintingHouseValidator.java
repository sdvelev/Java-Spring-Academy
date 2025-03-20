package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.EmptyValueException;
import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.exception.WrongDateIntervalException;
import com.digital.rewe.academy.printinghouse.publication.Publication;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PrintingHouseValidator {
    public void validateTitle(String title) {
        if (title == null) {
            throw new NullValueException("The title of the publication cannot be null");
        }

        if (title.isBlank()) {
            throw new EmptyValueException("The title of the publication cannot be empty or blank");
        }
    }

    public void validatePublishingDate(LocalDate publishingDate) {
        if (publishingDate == null) {
            throw new NullValueException("The publishing date of the publication cannot be null");
        }
    }

    public void validateNumberOfPages(long numberOfPages) {
        if (numberOfPages <= 0) {
            throw new NegativeValueException("The number of pages of the publication must be positive");
        }
    }

    public void validatePaperSize(PaperSize paperSize) {
        if (paperSize == null) {
            throw new NullValueException("The paper size of the publication cannot be null");
        }
    }

    public void validateDateArguments(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new NullValueException("The start date and the end date cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new WrongDateIntervalException("The start date argument cannot be after end date");
        }
    }

    public void validatePublication(Publication publicationToPrint) {
        if (publicationToPrint == null) {
            throw new NullValueException("The publication argument cannot be null");
        }
    }

    public void validateNumberOfCopies(long numberOfCopies) {
        if (numberOfCopies < 1) {
            throw new NegativeValueException("The argument for number of copies must be positive");
        }
    }

    public void validatePrintingHouse(PrintingHouse printingHouse) {
        if (printingHouse == null) {
            throw new NullValueException("The printing house argument cannot be null");
        }
    }

    public void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new NullValueException("The price argument cannot be null");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("The price argument must be positive");
        }
    }

    public void validateHouseName(String title) {
        if (title == null) {
            throw new NullValueException("The title of the house name cannot be null");
        }

        if (title.isBlank()) {
            throw new EmptyValueException("The title of the house name cannot be empty or blank");
        }
    }
}