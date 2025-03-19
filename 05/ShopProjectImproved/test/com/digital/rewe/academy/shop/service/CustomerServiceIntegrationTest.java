package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.CustomerServiceAPI;
import com.digital.rewe.academy.shop.api.ShopOperationsServiceAPI;
import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CustomerValidator;
import com.digital.rewe.academy.shop.validation.GoodValidator;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerServiceIntegrationTest {

    private ShopValidator shopValidator;
    private ShopOperationsServiceAPI shopOperationsService;
    private CustomerServiceAPI customerService;
    private Shop shop;

    @BeforeEach
    void setUp() {
        shopValidator = new ShopValidator();
        shopOperationsService = new ShopOperationsService(shopValidator);
        customerService = new CustomerService(new CustomerValidator(), shopOperationsService);
        shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), new ShopValidator());
    }

    @Test
    void testAddToBasketPositiveCase() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), new GoodValidator(), LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), new GoodValidator(),
            LocalDate.of(2025, 12, 1));

        Map<Good, Double> newGoodsMap = new HashMap<>();
        newGoodsMap.put(water, 6.);
        newGoodsMap.put(book, 1.);

        shopOperationsService.loadNewGoods(shop, newGoodsMap);

        customerService.addToBasket(customer, shop, water, 3.);
        Map<Good, Double> expectedBasket = Map.of(water, 3.);

        assertIterableEquals(expectedBasket.entrySet(), customer.getBasket().entrySet(),
            "The expected basket after adding is not the same as the actual");
    }

    @Test
    void testAddToBasketWithGoodNullValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());

        assertThrows(NullValueException.class, () -> customerService.addToBasket(customer, shop, null, 6),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testAddToBasketWithNegativeQuantity() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), new GoodValidator(), LocalDate.of(2025, 12, 1));

        assertThrows(NegativeValueException.class, () -> customerService.addToBasket(customer, shop, water, -6),
            "NegativeValueException expected to be thrown when negative value is given");
    }

    @Test
    void removeFromBasketPositiveCaseEverythingRemoved() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), new GoodValidator(), LocalDate.of(2025, 12, 1));

        shopOperationsService.loadNewGood(shop, water, 10.);

        customerService.addToBasket(customer, shop, water, 6);

        customerService.removeFromBasket(customer, shop, water, 6.);
        assertTrue(customer.getBasket().isEmpty(),
            "The basket of the customer is expected to be empty but it is not");
    }

    @Test
    void removeFromBasketPositiveCasePartiallyRemoved() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), new GoodValidator(), LocalDate.of(2025, 12, 1));

        shopOperationsService.loadNewGood(shop, water, 10.);

        customerService.addToBasket(customer, shop, water, 6);

        customerService.removeFromBasket(customer, shop, water, 4.);
        assertEquals(2, customer.getBasket().get(water),
            "Two elements are expected to be in the basket of the customer but they are not");
    }

    @Test
    void testRemoveFromBasketWithGoodNullValue() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());

        assertThrows(NullValueException.class, () -> customerService.removeFromBasket(customer, shop, null, 6),
            "NullValueException expected to be thrown when null value is given");
    }

    @Test
    void testRemoveFromBasketWithNegativeQuantity() {
        Customer customer = new Customer(1, BigDecimal.valueOf(100), new CustomerValidator());
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), new GoodValidator(), LocalDate.of(2025, 12, 1));

        assertThrows(NegativeValueException.class, () -> customerService.removeFromBasket(customer, shop, water, -6),
            "NegativeValueException expected to be thrown when negative value is given");
    }
}