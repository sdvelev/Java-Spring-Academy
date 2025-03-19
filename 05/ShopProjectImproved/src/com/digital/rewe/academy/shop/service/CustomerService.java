package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.api.CustomerServiceAPI;
import com.digital.rewe.academy.shop.api.ShopOperationsServiceAPI;
import com.digital.rewe.academy.shop.model.Customer;
import com.digital.rewe.academy.shop.model.Good;
import com.digital.rewe.academy.shop.model.Shop;
import com.digital.rewe.academy.shop.validation.CustomerValidator;

public class CustomerService implements CustomerServiceAPI {

    private CustomerValidator customerValidator;
    private ShopOperationsServiceAPI shopOperationsService;

    public CustomerService(CustomerValidator customerValidator, ShopOperationsServiceAPI shopOperationsService) {
        this.customerValidator = customerValidator;
        this.shopOperationsService = shopOperationsService;
    }

    @Override
    public void addToBasket(Customer customer, Shop shop, Good good, double amount) {
        customerValidator.validateGood(good);
        customerValidator.validateAmount(amount);
        shopOperationsService.removeGood(shop, good, amount);
        customer.getBasket().putIfAbsent(good, 0.);
        customer.getBasket().merge(good, amount, Double::sum);
    }

    @Override
    public boolean removeFromBasket(Customer customer, Shop shop, Good good, double amount) {
        customerValidator.validateGood(good);
        customerValidator.validateAmount(amount);
        if (customer.getBasket().containsKey(good) && customer.getBasket().get(good) > amount) {
            customer.getBasket().put(good, customer.getBasket().get(good) - amount);
            shopOperationsService.returnGood(shop, good, amount);
            return true;
        } else if (customer.getBasket().containsKey(good) && customer.getBasket().get(good) == amount) {
            customer.getBasket().remove(good);
            shopOperationsService.returnGood(shop, good, amount);
            return true;
        }
        return false;
    }
}