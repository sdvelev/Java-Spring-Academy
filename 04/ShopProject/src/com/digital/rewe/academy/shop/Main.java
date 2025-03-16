package com.digital.rewe.academy.shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Shop shop = new Shop(1, "My shop", 6, BigDecimal.valueOf(0.2));
        shop.addMarkupPercentage(GoodCategory.FOOD,  BigDecimal.valueOf(0.2));
        shop.addMarkupPercentage(GoodCategory.NON_FOOD, BigDecimal.valueOf(0.3));

        Cashier cashier = new Cashier(1, "Ivan",  BigDecimal.valueOf(2600));

        shop.addCashier(cashier);

        CashDesk cashDesk = new CashDesk(1, 6, shop);
        shop.addCashDesk(cashDesk);

        cashDesk.openCashDesk(cashier);

        Good water = new Good(1, "Water 1L", GoodCategory.FOOD, GoodUnit.LITER, BigDecimal.valueOf(4.6),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 12, 1));

        Good bread = new Good(2, "Bread 500GR", GoodCategory.FOOD, GoodUnit.PIECE, BigDecimal.valueOf(2.4),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 3, 14));

        Good orange = new Good(3, "Orange 1KG", GoodCategory.FOOD, GoodUnit.KILOGRAM, BigDecimal.valueOf(2.2),
            BigDecimal.valueOf(0.1), LocalDate.of(2025, 3, 26));

        shop.loadNewGoods(Map.of(water, 10., bread, 20., orange, 40.));


        Customer customer = new Customer(1, BigDecimal.valueOf(100), shop);
        customer.addToBasket(water, 2);
        customer.addToBasket(bread, 1);
        customer.addToBasket(orange, 3.6);

        cashDesk.addCustomerWaiting(customer);
        try {
            System.out.println("Customer Receipt: " + cashDesk.serveNextCustomer());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Shop Costs for Today: " + shop.getCostsForDay(LocalDate.now()));
        System.out.println("Shop Income for Today: " + shop.getIncomePerDay(LocalDate.now()));
        System.out.println("Shop Profit for Today: " + shop.getProfitPerDay(LocalDate.now()));

        System.out.println("----------------------");
        try {
            System.out.println(ReceiptService.deserializeReceipt("./receipts/RECEIPT_1.ser"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}