package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.exception.EmptyValueException;
import com.digital.rewe.academy.printinghouse.exception.NullValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseIntegrationTest {

    private PrintingHouseValidator printingHouseValidator;
    private PrintingHouse printingHouse;

    @BeforeEach
    void setUp() {
        printingHouseValidator = new PrintingHouseValidator();
        printingHouse = new PrintingHouse(1, "My printing house", printingHouseValidator);
    }

    @Test
    void testSetTitlePositiveCase() {
        String newTitle = "My new printing house";
        printingHouse.setHouseName(newTitle);

        assertEquals(newTitle, printingHouse.getHouseName(),
            "The name of the printing house is expected to change but remains the old one");
    }

    @Test
    void testSetTitleWithNullValue() {
        assertThrows(NullValueException.class, () -> printingHouse.setHouseName(null),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testSetTitleWithBlankValue() {
        assertThrows(EmptyValueException.class, () -> printingHouse.setHouseName("         "),
            "EmptyValueException expected to be thrown when blank value is given");
    }
}