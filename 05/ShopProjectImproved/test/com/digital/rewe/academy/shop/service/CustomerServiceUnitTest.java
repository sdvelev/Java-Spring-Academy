package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class CustomerServiceUnitTest {
    @Mock
    private CustomerValidator customerValidatorMock = Mockito.mock(CustomerValidator.class);

    @Mock
    private ShopOperationsService shopOperationsServiceMock = Mockito.mock(ShopOperationsService.class);

    @Mock
    private Shop shopMock;

    @Mock
    private Good goodMock;

    private CustomerService customerService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerValidatorMock, shopOperationsServiceMock);
        shopMock = Mockito.mock(Shop.class);
        goodMock = Mockito.mock(Good.class);
    }

    @Test
    void testAddToBasketPositiveCase() throws IllegalAccessException {
        customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());

        Mockito.doNothing().when(customerValidatorMock).validateGood(any());
        Mockito.doNothing().when(customerValidatorMock).validateAmount(6.);
        Mockito.doNothing().when(shopOperationsServiceMock).removeGood(shopMock, goodMock, 6.);

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(goodMock, 6.);

        customerService.addToBasket(customer, shopMock, goodMock, 6.);

        Field field = ReflectionUtils.findFields(Customer.class, f -> f.getName().equals("basket"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Map<Good, Double> actualBasket = (Map<Good, Double>) field.get(customer);
        Mockito.verify(customerValidatorMock, Mockito.times(1)).validateGood(Mockito.any());
        Mockito.verify(customerValidatorMock, Mockito.times(1)).validateAmount(6.);
        Mockito.verify(shopOperationsServiceMock, Mockito.times(1)).removeGood(shopMock, goodMock, 6.);

        assertIterableEquals(newGoodsMap.entrySet(), actualBasket.entrySet(),
            "The expected basket after adding is not the same as the actual");
    }

    @Test
    void testAddToBasketWithNegativeAmount() throws IllegalAccessException {
        customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());

        Mockito.doNothing().when(customerValidatorMock).validateGood(any());
        Mockito.doThrow(NegativeValueException.class).when(customerValidatorMock).validateAmount(-6.);

        assertThrows(NegativeValueException.class, () -> customerService.addToBasket(customer, shopMock, goodMock, -6.),
            "NegativeValueException is expected to be thrown");

        Field field = ReflectionUtils.findFields(Customer.class, f -> f.getName().equals("basket"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        Map<Good, Double> actualBasket = (Map<Good, Double>) field.get(customer);
        Mockito.verify(customerValidatorMock, Mockito.times(1)).validateGood(Mockito.any());
        Mockito.verify(customerValidatorMock, Mockito.times(1)).validateAmount(-6.);
        Mockito.verify(shopOperationsServiceMock, Mockito.never()).removeGood(shopMock, goodMock, -6.);

        assertTrue(actualBasket.isEmpty(), "The expected basket is expected to be empty but it is not");
    }

    @Test
    void testRemoveFromBasketPositiveCasePartiallyRemoved() {
        customer = Mockito.mock(Customer.class);

        Mockito.doNothing().when(customerValidatorMock).validateGood(any());
        Mockito.doNothing().when(customerValidatorMock).validateAmount(6.);
        Mockito.doNothing().when(shopOperationsServiceMock).removeGood(shopMock, goodMock, 6.);

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(goodMock, 10.);
        Mockito.when(customer.getBasket()).thenReturn(newGoodsMap);

        customerService.removeFromBasket(customer, shopMock, goodMock, 6.);

        assertEquals(4, customer.getBasket().get(goodMock),
            "The actual number of goods is not the same as the expected after removal from basket");
        Mockito.verify(shopOperationsServiceMock).returnGood(shopMock, goodMock, 6.);
    }

    @Test
    void testRemoveFromBasketPositiveCaseEverythingRemoved() {
        customer = Mockito.mock(Customer.class);

        Mockito.doNothing().when(customerValidatorMock).validateGood(any());
        Mockito.doNothing().when(customerValidatorMock).validateAmount(6.);
        Mockito.doNothing().when(shopOperationsServiceMock).removeGood(shopMock, goodMock, 6.);

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(goodMock, 6.);
        Mockito.when(customer.getBasket()).thenReturn(newGoodsMap);

        customerService.removeFromBasket(customer, shopMock, goodMock, 6.);

        assertTrue(customer.getBasket().isEmpty(),
            "The basket of the customer is expected to be empty after removal but it is not");
        Mockito.verify(shopOperationsServiceMock).returnGood(shopMock, goodMock, 6.);
    }
}