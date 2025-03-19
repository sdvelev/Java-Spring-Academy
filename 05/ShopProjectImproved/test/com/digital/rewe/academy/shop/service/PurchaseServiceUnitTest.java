package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.GoodValidator;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class PurchaseServiceUnitTest {

    private static final int ROUNDING_SCALE_FACTOR = 2;

    @Mock
    private Shop shopMock = Mockito.mock(Shop.class);
    @Mock
    private Cashier cashierMock = Mockito.mock(Cashier.class);
    @Mock
    private CashDesk cashDeskMock = Mockito.mock(CashDesk.class);
    @Mock
    private Customer customerMock = Mockito.mock(Customer.class);

    @Mock
    private GoodValidator goodValidatorMock;
    @Mock
    private CashDeskValidator cashDeskValidatorMock;
    @Mock
    private ReceiptValidator receiptValidatorMock;
    @Mock
    private ShopValidator shopValidatorMock;

    private PurchaseService purchaseService;
    private ShopOperationsService shopOperationsService;

    @BeforeEach
    void setUp() throws IOException, NotEnoughMoneyException {
        goodValidatorMock = Mockito.mock(GoodValidator.class);
        cashDeskValidatorMock = Mockito.mock(CashDeskValidator.class);
        receiptValidatorMock = Mockito.mock(ReceiptValidator.class);
        shopValidatorMock = Mockito.mock(ShopValidator.class);
        purchaseService = new PurchaseService(cashDeskValidatorMock, receiptValidatorMock, shopValidatorMock);
        shopOperationsService = new ShopOperationsService(shopValidatorMock);
    }

    @Test
    void testServeNextCustomerWithoutExpiryDateDiscount() throws IOException {
        Mockito.when(cashDeskMock.getId()).thenReturn(1L);
        Mockito.when(cashDeskMock.getCashDeskNumber()).thenReturn(5);

        Good waterMock = Mockito.mock(Good.class);
        Mockito.when(waterMock.getId()).thenReturn(1L);
        Mockito.when(waterMock.getName()).thenReturn("Water 1L");
        Mockito.when(waterMock.getGoodCategory()).thenReturn(GoodCategory.FOOD);
        Mockito.when(waterMock.getGoodUnit()).thenReturn(GoodUnit.LITER);
        Mockito.when(waterMock.getPurchasePrice()).thenReturn(BigDecimal.valueOf(1.6));
        Mockito.when(waterMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));
        Mockito.when(waterMock.getExpiryDate()).thenReturn(LocalDate.of(2025, 12, 1));

        Good bookMock = Mockito.mock(Good.class);
        Mockito.when(bookMock.getId()).thenReturn(2L);
        Mockito.when(bookMock.getName()).thenReturn("And Then There Were None | Agatha Christie");
        Mockito.when(bookMock.getGoodCategory()).thenReturn(GoodCategory.NON_FOOD);
        Mockito.when(bookMock.getGoodUnit()).thenReturn(GoodUnit.PIECE);
        Mockito.when(bookMock.getPurchasePrice()).thenReturn(BigDecimal.valueOf(19.9));
        Mockito.when(bookMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));

        Mockito.when(customerMock.getAvailableMoney()).thenReturn(BigDecimal.valueOf(100));

        Map<Good, Double> basket = new LinkedHashMap<>();
        basket.put(waterMock, 6.);
        basket.put(bookMock, 1.);

        Mockito.when(customerMock.getBasket()).thenReturn(basket);
        Mockito.when(cashDeskMock.isOpen()).thenReturn(true);
        Mockito.when(shopMock.getName()).thenReturn("My shop");

        Map<GoodCategory, BigDecimal> toBeReturnedMarkupMap = new HashMap<>();
        toBeReturnedMarkupMap.put(GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        toBeReturnedMarkupMap.put(GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        Mockito.when(shopMock.getMarkupPercentages()).thenReturn(toBeReturnedMarkupMap);
        Mockito.when(cashierMock.getId()).thenReturn(10L);
        Mockito.when(cashierMock.getName()).thenReturn("Mark");

        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDesk(cashDeskMock);
        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDescOpen(cashDeskMock);
        Mockito.doNothing().when(cashDeskValidatorMock).validateCustomersWaiting(cashDeskMock);

        Set<Customer> toBeReturnedSet = new LinkedHashSet<>();
        toBeReturnedSet.add(customerMock);
        Mockito.when(cashDeskMock.getWaitingCustomers()).thenReturn(toBeReturnedSet);

        Mockito.when(cashDeskMock.getWorkingCashier()).thenReturn(cashierMock);
        Mockito.when(cashDeskMock.getCashDeskNumber()).thenReturn(5);

        Mockito.when(shopMock.getDaysBeforeExpiryDateDiscount()).thenReturn(7);

        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any(GoodCategory.class));

        Receipt returnedReceipt = purchaseService.serveNextCustomer(shopMock, cashDeskMock);

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
    }

    @Test
    void testServeNextCustomerWithExpiryDateDiscount() throws IOException {
        Mockito.when(cashDeskMock.getId()).thenReturn(1L);
        Mockito.when(cashDeskMock.getCashDeskNumber()).thenReturn(5);

        Good waterMock = Mockito.mock(Good.class);
        Mockito.when(waterMock.getId()).thenReturn(1L);
        Mockito.when(waterMock.getName()).thenReturn("Water 1L");
        Mockito.when(waterMock.getGoodCategory()).thenReturn(GoodCategory.FOOD);
        Mockito.when(waterMock.getGoodUnit()).thenReturn(GoodUnit.LITER);
        Mockito.when(waterMock.getPurchasePrice()).thenReturn(BigDecimal.valueOf(1.6));
        Mockito.when(waterMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));
        Mockito.when(waterMock.getExpiryDate()).thenReturn(LocalDate.of(2025, 3, 25));

        Good bookMock = Mockito.mock(Good.class);
        Mockito.when(bookMock.getId()).thenReturn(2L);
        Mockito.when(bookMock.getName()).thenReturn("And Then There Were None | Agatha Christie");
        Mockito.when(bookMock.getGoodCategory()).thenReturn(GoodCategory.NON_FOOD);
        Mockito.when(bookMock.getGoodUnit()).thenReturn(GoodUnit.PIECE);
        Mockito.when(bookMock.getPurchasePrice()).thenReturn(BigDecimal.valueOf(19.9));
        Mockito.when(bookMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));


        Mockito.when(customerMock.getAvailableMoney()).thenReturn(BigDecimal.valueOf(100));

        Map<Good, Double> basket = new LinkedHashMap<>();
        basket.put(waterMock, 6.);
        basket.put(bookMock, 1.);

        Mockito.when(customerMock.getBasket()).thenReturn(basket);

        Mockito.when(cashDeskMock.isOpen()).thenReturn(true);

        Mockito.when(shopMock.getName()).thenReturn("My shop");

        Map<GoodCategory, BigDecimal> toBeReturnedMarkupMap = new HashMap<>();
        toBeReturnedMarkupMap.put(GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        toBeReturnedMarkupMap.put(GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        Mockito.when(shopMock.getMarkupPercentages()).thenReturn(toBeReturnedMarkupMap);

        Mockito.when(cashierMock.getId()).thenReturn(10L);
        Mockito.when(cashierMock.getName()).thenReturn("Mark");

        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDesk(cashDeskMock);
        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDescOpen(cashDeskMock);
        Mockito.doNothing().when(cashDeskValidatorMock).validateCustomersWaiting(cashDeskMock);

        Set<Customer> toBeReturnedSet = new LinkedHashSet<>();
        toBeReturnedSet.add(customerMock);
        Mockito.when(cashDeskMock.getWaitingCustomers()).thenReturn(toBeReturnedSet);

        Mockito.when(cashDeskMock.getWorkingCashier()).thenReturn(cashierMock);
        Mockito.when(cashDeskMock.getCashDeskNumber()).thenReturn(5);

        Mockito.when(shopMock.getDaysBeforeExpiryDateDiscount()).thenReturn(7);
        Mockito.when(shopMock.getDiscountExpiryDatePercentage()).thenReturn(BigDecimal.valueOf(0.2));

        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any(GoodCategory.class));

        Receipt returnedReceipt = purchaseService.serveNextCustomer(shopMock, cashDeskMock);

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
    }

    @Test
    void testServeNextCustomerWithNotEnoughMoney() throws IOException {
        Mockito.when(cashDeskMock.getId()).thenReturn(1L);
        Mockito.when(cashDeskMock.getCashDeskNumber()).thenReturn(5);

        Good waterMock = Mockito.mock(Good.class);
        Mockito.when(waterMock.getId()).thenReturn(1L);
        Mockito.when(waterMock.getName()).thenReturn("Water 1L");
        Mockito.when(waterMock.getGoodCategory()).thenReturn(GoodCategory.FOOD);
        Mockito.when(waterMock.getGoodUnit()).thenReturn(GoodUnit.LITER);
        Mockito.when(waterMock.getPurchasePrice()).thenReturn(BigDecimal.valueOf(1.6));
        Mockito.when(waterMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));
        Mockito.when(waterMock.getExpiryDate()).thenReturn(LocalDate.of(2025, 12, 1));

        Good bookMock = Mockito.mock(Good.class);
        Mockito.when(bookMock.getId()).thenReturn(2L);
        Mockito.when(bookMock.getName()).thenReturn("And Then There Were None | Agatha Christie");
        Mockito.when(bookMock.getGoodCategory()).thenReturn(GoodCategory.NON_FOOD);
        Mockito.when(bookMock.getGoodUnit()).thenReturn(GoodUnit.PIECE);
        Mockito.when(bookMock.getPurchasePrice()).thenReturn(BigDecimal.valueOf(19.9));
        Mockito.when(bookMock.getDeliveryPercentage()).thenReturn(BigDecimal.valueOf(0.1));

        Mockito.when(customerMock.getAvailableMoney()).thenReturn(BigDecimal.valueOf(20));

        Map<Good, Double> basket = new LinkedHashMap<>();
        basket.put(waterMock, 6.);
        basket.put(bookMock, 1.);

        Mockito.when(customerMock.getBasket()).thenReturn(basket);

        Mockito.when(cashDeskMock.isOpen()).thenReturn(true);

        Mockito.when(shopMock.getName()).thenReturn("My shop");

        Map<GoodCategory, BigDecimal> toBeReturnedMarkupMap = new HashMap<>();
        toBeReturnedMarkupMap.put(GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        toBeReturnedMarkupMap.put(GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        Mockito.when(shopMock.getMarkupPercentages()).thenReturn(toBeReturnedMarkupMap);

        Mockito.when(cashierMock.getId()).thenReturn(10L);
        Mockito.when(cashierMock.getName()).thenReturn("Mark");

        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDesk(cashDeskMock);
        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDescOpen(cashDeskMock);
        Mockito.doNothing().when(cashDeskValidatorMock).validateCustomersWaiting(cashDeskMock);

        Set<Customer> toBeReturnedSet = new LinkedHashSet<>();
        toBeReturnedSet.add(customerMock);
        Mockito.when(cashDeskMock.getWaitingCustomers()).thenReturn(toBeReturnedSet);

        Mockito.when(cashDeskMock.getWorkingCashier()).thenReturn(cashierMock);
        Mockito.when(cashDeskMock.getCashDeskNumber()).thenReturn(5);

        Mockito.when(shopMock.getDaysBeforeExpiryDateDiscount()).thenReturn(7);

        Mockito.doNothing().when(shopValidatorMock).validateGoodCategory(any(GoodCategory.class));

        assertThrows(NotEnoughMoneyException.class, () -> purchaseService.serveNextCustomer(shopMock, cashDeskMock),
            "NotEnoughMoneyException is expected but is not thrown");

        Mockito.verify(customerMock, Mockito.never()).setAvailableMoney(any());
        Mockito.verify(shopMock, Mockito.never()).getIncomePerDayMap();
    }

    @Test
    void testGetFirstCustomerPositiveCase() {
        Mockito.doNothing().when(cashDeskValidatorMock).validateCashDesk(any());
        Mockito.when(cashDeskMock.isOpen()).thenReturn(true);
        Set<Customer> setToReturn = new LinkedHashSet<>();
        setToReturn.add(customerMock);
        Mockito.when(cashDeskMock.getWaitingCustomers()).thenReturn(setToReturn);

        assertEquals(customerMock, purchaseService.getFirstCustomer(cashDeskMock),
            "The returned customer is not the same as the expected");
        assertTrue(setToReturn.isEmpty(), "The set of customers is expected to be empty but is not");
    }
}