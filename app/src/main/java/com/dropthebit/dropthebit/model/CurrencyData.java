package com.dropthebit.dropthebit.model;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class CurrencyData {
    private CurrencyType type;
    private String name;
    private String price;
    private String maxPrice;
    private String minPrice;

    public CurrencyData() {
    }

    public CurrencyData(CurrencyType type, String name, String price, String maxPrice, String minPrice) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public CurrencyType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }
}
