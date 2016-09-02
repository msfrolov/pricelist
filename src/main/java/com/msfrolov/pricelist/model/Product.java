package com.msfrolov.pricelist.model;

import java.math.BigDecimal;

public class Product extends BaseEntity {

    private String name;
    private Category category;
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override public String toString() {
        return "Product{" + "id=" + getId() + ", name='" + name + '\'' + ", category=" + category + "," + " price="
                + price + '}';
    }
}
