package com.digital.rewe.academy.printinghouse.order;

import com.digital.rewe.academy.printinghouse.publication.Publication;

import java.time.LocalDate;

public record Order(long id, Publication publication, long numberOfCopies, LocalDate dateOfOrder) {
    public Order {
        if (publication == null) {
            throw new IllegalArgumentException("The argument for application cannot be null");
        }

        if (dateOfOrder.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The argument for order date cannot be after the current date");
        }
    }
}