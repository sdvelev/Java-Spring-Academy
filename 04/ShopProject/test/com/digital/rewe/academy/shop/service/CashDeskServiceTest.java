package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.Shop;
import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Receipt;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CashDeskServiceTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private Shop shopMock = Mockito.mock(Shop.class);
    private Cashier cashierMock = Mockito.mock(Cashier.class);

    @Test
    void testServeNextCustomerWithoutExpiryDateDiscount() throws IOException {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        customer.addToBasket(water, 6);
        customer.addToBasket(book, 1);

        cashDesk.openCashDesk(cashierMock);
        cashDesk.addCustomerWaiting(customer);

        Mockito.when(shopMock.getName()).thenReturn("My shop");
        Mockito.when(shopMock.getMarkupPercentage(GoodCategory.FOOD)).thenReturn(BigDecimal.valueOf(0.1));
        Mockito.when(shopMock.getMarkupPercentage(GoodCategory.NON_FOOD)).thenReturn(BigDecimal.valueOf(0.2));

        Mockito.when(cashierMock.getId()).thenReturn(10L);
        Mockito.when(cashierMock.getName()).thenReturn("Mark");

        Receipt returnedReceipt = CashDeskService.serveNextCustomer(cashDesk);

        assertEquals("My shop", returnedReceipt.getShopName(),
            "Actual shop name is not the same as the expected");

        assertEquals(10L, returnedReceipt.getCashierServing().getId(),
            "Actual cashier ID is not the same as the expected");

        assertEquals(10L, returnedReceipt.getCashierServing().getId(),
            "Actual cashier name is not the same as the expected");

        assertEquals(5, returnedReceipt.getCashDeskNumber(),
            "Actual cash desk number is not the same as the expected");

        assertEquals(LocalDate.now(), returnedReceipt.getDateOfPurchase(),
            "Actual date of purchase is not the same as the expected");

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodWithQuantityMap = new LinkedHashMap<>();
        expectedGoodWithQuantityMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.76)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 6.));
        expectedGoodWithQuantityMap.put("And Then There Were None | Agatha Christie",
            new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(23.88).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 1.));

        assertIterableEquals(expectedGoodWithQuantityMap.entrySet(), returnedReceipt.getGoodWithQuantityMap().entrySet(),
            "Actual goods with quantity map is not the same as the expected");

        assertEquals(BigDecimal.valueOf(34.44).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            returnedReceipt.getTotalPrice(),
            "Actual total price is not the same as the expected");


        Mockito.verify(shopMock, Mockito.times(1)).getName();
        Mockito.verify(shopMock, Mockito.times(1)).getMarkupPercentage(GoodCategory.FOOD);
        Mockito.verify(shopMock, Mockito.times(1)).getMarkupPercentage(GoodCategory.NON_FOOD);
        Mockito.verify(cashierMock, Mockito.times(1)).getName();
    }

    @Test
    void testServeNextCustomerWithExpiryDateDiscount() throws IOException {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 3, 21));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Customer customer = new Customer(1, BigDecimal.valueOf(100), shopMock);
        customer.addToBasket(water, 6);
        customer.addToBasket(book, 1);

        cashDesk.openCashDesk(cashierMock);
        cashDesk.addCustomerWaiting(customer);

        Mockito.when(shopMock.getName()).thenReturn("My shop");
        Mockito.when(shopMock.getMarkupPercentage(GoodCategory.FOOD)).thenReturn(BigDecimal.valueOf(0.1));
        Mockito.when(shopMock.getMarkupPercentage(GoodCategory.NON_FOOD)).thenReturn(BigDecimal.valueOf(0.2));
        Mockito.when(shopMock.getDaysBeforeExpiryDateDiscount()).thenReturn(7);
        Mockito.when(shopMock.getDiscountExpiryDatePercentage()).thenReturn(BigDecimal.valueOf(0.2));

        Mockito.when(cashierMock.getId()).thenReturn(10L);
        Mockito.when(cashierMock.getName()).thenReturn("Mark");

        Receipt returnedReceipt = CashDeskService.serveNextCustomer(cashDesk);

        assertEquals("My shop", returnedReceipt.getShopName(),
            "Actual shop name is not the same as the expected");

        assertEquals(10L, returnedReceipt.getCashierServing().getId(),
            "Actual cashier ID is not the same as the expected");

        assertEquals(10L, returnedReceipt.getCashierServing().getId(),
            "Actual cashier name is not the same as the expected");

        assertEquals(5, returnedReceipt.getCashDeskNumber(),
            "Actual cash desk number is not the same as the expected");

        assertEquals(LocalDate.now(), returnedReceipt.getDateOfPurchase(),
            "Actual date of purchase is not the same as the expected");

        Map<String, Map.Entry<BigDecimal, Double>> expectedGoodWithQuantityMap = new LinkedHashMap<>();
        expectedGoodWithQuantityMap.put("Water 1L", new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(1.408)
            .setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 6.));
        expectedGoodWithQuantityMap.put("And Then There Were None | Agatha Christie",
            new AbstractMap.SimpleEntry<>(BigDecimal.valueOf(23.88).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP), 1.));

        assertIterableEquals(expectedGoodWithQuantityMap.entrySet(), returnedReceipt.getGoodWithQuantityMap().entrySet(),
            "Actual goods with quantity map is not the same as the expected");

        assertEquals(BigDecimal.valueOf(32.328).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            returnedReceipt.getTotalPrice(),
            "Actual total price is not the same as the expected");

        Mockito.verify(shopMock, Mockito.times(1)).getName();
        Mockito.verify(shopMock, Mockito.times(1)).getMarkupPercentage(GoodCategory.FOOD);
        Mockito.verify(shopMock, Mockito.times(1)).getMarkupPercentage(GoodCategory.NON_FOOD);
        Mockito.verify(cashierMock, Mockito.times(1)).getName();
        Mockito.verify(shopMock, Mockito.times(1)).getDaysBeforeExpiryDateDiscount();
        Mockito.verify(shopMock, Mockito.times(1)).getDiscountExpiryDatePercentage();
    }

    @Test
    void testServeNextCustomerWithNotEnoughMoney() throws IOException {
        CashDesk cashDesk = new CashDesk(1, 5, shopMock);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2026, 8, 19));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1));

        Customer customer = new Customer(1, BigDecimal.valueOf(20), shopMock);
        customer.addToBasket(water, 10);
        customer.addToBasket(book, 1);

        cashDesk.openCashDesk(cashierMock);
        cashDesk.addCustomerWaiting(customer);

        Mockito.when(shopMock.getMarkupPercentage(GoodCategory.FOOD)).thenReturn(BigDecimal.valueOf(0.1));
        Mockito.when(shopMock.getMarkupPercentage(GoodCategory.NON_FOOD)).thenReturn(BigDecimal.valueOf(0.2));
        Mockito.when(shopMock.getDaysBeforeExpiryDateDiscount()).thenReturn(7);
        Mockito.when(shopMock.getDiscountExpiryDatePercentage()).thenReturn(BigDecimal.valueOf(0.2));

        assertThrows(NotEnoughMoneyException.class, () -> CashDeskService.serveNextCustomer(cashDesk),
            "NotEnoughMoneyException is expected but is not thrown");

        assertEquals(BigDecimal.valueOf(20).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            customer.getAvailableMoney(), "The customer is expected tp have the same money as before");

        assertTrue(customer.getBasket().isEmpty(), "The customer basket is expected to be empty but is not");

        Mockito.verify(shopMock, Mockito.times(1)).returnGood(water, 10.);
        Mockito.verify(shopMock, Mockito.times(1)).returnGood(book, 1.);
    }
}
