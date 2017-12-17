package com.dropthebit.dropthebit.model;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class CurrencyData {
    private String name;
    private String price;
    private String maxPrice;
    private String minPrice;

    public CurrencyData() {
    }

    public CurrencyData(String name, String price, String maxPrice, String minPrice) {
        this.name = name;
        this.price = price;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }
}
