package com.digital.rewe.academy.printinghouse;

import com.digital.rewe.academy.printinghouse.order.Order;
import com.digital.rewe.academy.printinghouse.tariff.PaperSize;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrintingHouse {

    private final long id;
    private String houseName;
    private final Set<Order> orders;
    private final Map<PaperSize, BigDecimal> paperPricePerSize;
    private final Map<PaperSize, BigDecimal> priceListPerSize;

    private long counterOfOrderInstances;

    private PrintingHouseValidator printingHouseValidator;

    public PrintingHouse(long id, String houseName, PrintingHouseValidator printingHouseValidator) {
        this.printingHouseValidator = printingHouseValidator;
        this.id = id;
        this.setHouseName(houseName);
        this.orders = new HashSet<>();
        this.paperPricePerSize = new EnumMap<>(PaperSize.class);
        this.priceListPerSize = new EnumMap<>(PaperSize.class);
        this.counterOfOrderInstances = 0;
    }

    public PrintingHouse(long id, String houseName, Set<Order> orders,  Map<PaperSize, BigDecimal> paperPricePerSize,
                         Map<PaperSize, BigDecimal> priceListPerSize, PrintingHouseValidator printingHouseValidator) {
        this.id = id;
        this.houseName = houseName;
        this.orders = orders;
        this.paperPricePerSize = paperPricePerSize;
        this.priceListPerSize = priceListPerSize;
        this.counterOfOrderInstances = 0;
        this.printingHouseValidator = printingHouseValidator;
    }

    public long getId() {
        return id;
    }

    public String getHouseName() {
        return houseName;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Map<PaperSize, BigDecimal> getPaperPricePerSize() {
        return paperPricePerSize;
    }

    public Map<PaperSize, BigDecimal> getPriceListPerSize() {
        return priceListPerSize;
    }

    public long getCounterOfOrderInstances() {
        return counterOfOrderInstances;
    }

    public void setHouseName(String houseName) {
        printingHouseValidator.validateHouseName(houseName);
        this.houseName = houseName;
    }

    public void setCounterOfOrderInstances(long counterOfOrderInstances) {
        this.counterOfOrderInstances = counterOfOrderInstances;
    }
}