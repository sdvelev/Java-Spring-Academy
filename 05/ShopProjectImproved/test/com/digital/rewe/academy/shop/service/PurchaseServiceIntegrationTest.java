package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.enums.GoodCategory;
import com.digital.rewe.academy.shop.enums.GoodUnit;
import com.digital.rewe.academy.shop.exception.NotEnoughMoneyException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.CashDesk;
import com.digital.rewe.academy.shop.model.Cashier;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CashDeskValidator;
import com.digital.rewe.academy.shop.validation.CashierValidator;
import com.digital.rewe.academy.shop.validation.CustomerValidator;
import com.digital.rewe.academy.shop.validation.GoodValidator;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import com.digital.rewe.academy.shop.validation.ShopValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseServiceIntegrationTest {
    private static final int ROUNDING_SCALE_FACTOR = 2;

    private CustomerValidator customerValidator;
    private CashDeskValidator cashDeskValidator;
    private GoodValidator goodValidator;
    private ShopValidator shopValidator;
    private ReceiptValidator receiptValidator;
    private CashierValidator cashierValidator;

    private CustomerService customerService;
    private ShopOperationsService shopOperationsService;
    private CashDeskService cashDeskService;
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() throws IOException, NotEnoughMoneyException {
        customerValidator = new CustomerValidator();
        cashDeskValidator = new CashDeskValidator();
        goodValidator = new GoodValidator();
        shopValidator = new ShopValidator();
        receiptValidator = new ReceiptValidator();
        cashierValidator = new CashierValidator();
        shopOperationsService = new ShopOperationsService(shopValidator);
        customerService = new CustomerService(customerValidator, shopOperationsService);
        cashDeskService = new CashDeskService(cashDeskValidator);
        purchaseService = new PurchaseService(cashDeskValidator, receiptValidator, shopValidator);
    }

    @Test
    void testServeNextCustomerWithoutExpiryDateDiscount() throws IOException {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 1));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.1), shopValidator);
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        shopOperationsService.loadNewGood(shop, water, 16);
        shopOperationsService.loadNewGood(shop, book, 10);

        Customer customer = new Customer(1, BigDecimal.valueOf(100), customerValidator);
        customerService.addToBasket(customer, shop, water, 6);
        customerService.addToBasket(customer, shop, book, 1);

        Cashier cashier = new Cashier(10L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        cashDesk.setWorkingCashier(cashier);
        cashDeskService.addCustomerWaiting(cashDesk, customer);


        Receipt returnedReceipt = purchaseService.serveNextCustomer(shop, cashDesk);

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
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 3, 26));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.2), shopValidator);
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        shopOperationsService.loadNewGood(shop, water, 16);
        shopOperationsService.loadNewGood(shop, book, 10);

        Customer customer = new Customer(1, BigDecimal.valueOf(100), customerValidator);
        customerService.addToBasket(customer, shop, water, 6);
        customerService.addToBasket(customer, shop, book, 1);

        Cashier cashier = new Cashier(10L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        cashDesk.setWorkingCashier(cashier);
        cashDeskService.addCustomerWaiting(cashDesk, customer);

        Receipt returnedReceipt = purchaseService.serveNextCustomer(shop, cashDesk);

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
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 26));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.2), shopValidator);
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        shopOperationsService.loadNewGood(shop, water, 16);
        shopOperationsService.loadNewGood(shop, book, 10);

        Customer customer = new Customer(1, BigDecimal.valueOf(10), customerValidator);
        customerService.addToBasket(customer, shop, water, 10);
        customerService.addToBasket(customer, shop, book, 1);

        Cashier cashier = new Cashier(10L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        cashDesk.setWorkingCashier(cashier);
        cashDeskService.addCustomerWaiting(cashDesk, customer);

        assertThrows(NotEnoughMoneyException.class, () -> purchaseService.serveNextCustomer(shop, cashDesk),
            "NotEnoughMoneyException is expected but is not thrown");

        assertEquals(BigDecimal.valueOf(10).setScale(ROUNDING_SCALE_FACTOR, RoundingMode.HALF_UP),
            customer.getAvailableMoney(), "The customer is expected to have the same money as before");

        assertTrue(customer.getBasket().isEmpty(), "The customer basket is expected to be empty but is not");
    }

    @Test
    void testGetFirstCustomerPositiveCase() {
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);
        Cashier cashier = new Cashier(10L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);

        cashDesk.setWorkingCashier(cashier);

        Customer firstCustomer = new Customer(1, BigDecimal.valueOf(10), customerValidator);
        Customer secondCustomer = new Customer(2, BigDecimal.valueOf(20), customerValidator);
        cashDeskService.addCustomerWaiting(cashDesk, firstCustomer);
        cashDeskService.addCustomerWaiting(cashDesk, secondCustomer);

        assertEquals(firstCustomer, purchaseService.getFirstCustomer(cashDesk),
            "The order of the customers at the cash desk is not the same as the expected");

        assertEquals(secondCustomer, purchaseService.getFirstCustomer(cashDesk),
            "The order of the customers at the cash desk is not the same as the expected");

        assertTrue(cashDesk.getWaitingCustomers().isEmpty(),
            "Cash desk is expected to be empty but it is not");
    }

    @Test
    void testReturnBasketPositiveCase() throws IOException {
        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(0.1), goodValidator, LocalDate.of(2025, 12, 26));
        Good book = new Good(2, "And Then There Were None | Agatha Christie", GoodCategory.NON_FOOD,
            GoodUnit.PIECE, BigDecimal.valueOf(19.9), BigDecimal.valueOf(0.1), goodValidator);

        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.2), shopValidator);
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.FOOD, BigDecimal.valueOf(0.1));
        shopOperationsService.addMarkupPercentage(shop, GoodCategory.NON_FOOD, BigDecimal.valueOf(0.2));

        shopOperationsService.loadNewGood(shop, water, 16);
        shopOperationsService.loadNewGood(shop, book, 10);

        Customer customer = new Customer(1, BigDecimal.valueOf(100), customerValidator);
        customerService.addToBasket(customer, shop, water, 10);
        customerService.addToBasket(customer, shop, book, 1);

        assertEquals(6., shop.getAvailableGoods().get(water),
            "The actual quantity of the goods in the shop is not the same as the expected");

        assertEquals(9, shop.getAvailableGoods().get(book),
            "The actual quantity of the goods in the shop is not the same as the expected");

        Cashier cashier = new Cashier(10L, "Mark", "My shop", BigDecimal.valueOf(2600), cashierValidator);
        CashDesk cashDesk = new CashDesk(1, 5, cashDeskValidator);

        cashDesk.setWorkingCashier(cashier);
        cashDeskService.addCustomerWaiting(cashDesk, customer);

        purchaseService.returnBasket(customer, shop);

        assertEquals(16., shop.getAvailableGoods().get(water),
            "The actual quantity of the goods in the shop is not the same as the expected");

        assertEquals(10, shop.getAvailableGoods().get(book),
            "The actual quantity of the goods in the shop is not the same as the expected");
    }

    @Test
    void testReturnBasketWithNullCustomer() throws IOException {
        Shop shop = new Shop(1, "My shop", 7, BigDecimal.valueOf(0.2), shopValidator);
        assertThrows(NullValueException.class, () -> purchaseService.returnBasket(null, shop),
            "NullValueException is expected but not thrown");
    }
}