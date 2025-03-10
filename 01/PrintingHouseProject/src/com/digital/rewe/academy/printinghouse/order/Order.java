package com.digital.rewe.academy.printinghouse.order;

import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.time.LocalDate;

public record Order(long id, Publication publication, long numberOfCopies, LocalDate dateOfOrder) {

    public Order {
        if (id < 0) {
            throw new IllegalArgumentException("The argument for id of the order must be positive");
        }

        if (publication == null) {
            throw new IllegalArgumentException("The argument for publication of the order cannot be null");
        }

        if (numberOfCopies <= 0) {
            throw new IllegalArgumentException("The argument for number of copies must be positive");
        }

        if (dateOfOrder == null || dateOfOrder.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The argument for the date of order cannot be null or after the current date");
        }
    }
}