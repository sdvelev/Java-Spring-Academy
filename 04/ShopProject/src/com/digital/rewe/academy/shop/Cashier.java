package com.digital.rewe.academy.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Cashier implements Serializable {
    private long id;
    private String name;
    private BigDecimal salary;
//    private Shop shop;

    public Cashier(long id, String name, BigDecimal salary/*, Shop shop*/) {
        this.id = id;
        this.name = name;
        this.salary = salary;
//        this.shop = shop;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

//    public Shop getShop() {
//        return shop;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

//    public void setShop(Shop shop) {
//        this.shop = shop;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return id == cashier.id && Objects.equals(name, cashier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Cashier{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", salary=" + salary +
            '}';
    }
}