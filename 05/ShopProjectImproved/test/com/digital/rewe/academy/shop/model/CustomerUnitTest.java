package com.digital.rewe.academy.shop.model;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.validation.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerUnitTest {

    @Mock
    private CustomerValidator customerValidatorMock = Mockito.mock(CustomerValidator.class);

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, BigDecimal.valueOf(100), customerValidatorMock);
    }

    @Test
    void testSetAvailableMoneyPositiveCase() throws IllegalAccessException {
        Mockito.doNothing().when(customerValidatorMock).validateAvailableMoney(Mockito.any());
        BigDecimal newAvailableMoney = BigDecimal.valueOf(600);
        customer.setAvailableMoney(newAvailableMoney);
        Field field = ReflectionUtils.findFields(Customer.class, f -> f.getName().equals("availableMoney"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualAvailableMoney = (BigDecimal) field.get(customer);
        Mockito.verify(customerValidatorMock, Mockito.times(2)).validateAvailableMoney(Mockito.any());
        assertEquals(newAvailableMoney, actualAvailableMoney,
            "The available money of the customer is expected to change but remains the old one");
    }

    @Test
    void testSetAvailableMoneyWithNegativeValue() throws IllegalAccessException {
        Mockito.doThrow(NegativeValueException.class).when(customerValidatorMock).validateAvailableMoney(Mockito.any());
        BigDecimal newAvailableMoney = BigDecimal.valueOf(-600);
        assertThrows(NegativeValueException.class, () -> customer.setAvailableMoney(newAvailableMoney),
            "NegativeValueException is expected to be thrown but it is not");
        Field field = ReflectionUtils.findFields(Customer.class, f -> f.getName().equals("availableMoney"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        BigDecimal actualAvailableMoney = (BigDecimal) field.get(customer);
        assertEquals(BigDecimal.valueOf(100), actualAvailableMoney,
            "The available money of the customer is not expected to change but it has");
    }
}