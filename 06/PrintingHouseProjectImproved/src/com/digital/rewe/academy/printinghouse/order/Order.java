package com.digital.rewe.academy.printinghouse.order;

import com.digital.rewe.academy.printinghouse.exception.NegativeValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.time.LocalDate;

public record Order(long id, Publication publication, long numberOfCopies, LocalDate dateOfOrder) {

    public Order {
        if (id < 0) {
            throw new NegativeValueException("The argument for id of the order must be positive");
        }

        if (publication == null) {
            throw new NullValueException("The argument for publication of the order cannot be null");
        }

        if (numberOfCopies <= 0) {
            throw new NegativeValueException("The argument for number of copies must be positive");
        }

        if (dateOfOrder == null) {
            throw new NullValueException("The argument for the date of order cannot be null");
        }
    }
}