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
    private String openingPrice;

    public CurrencyData() {
    }

    public CurrencyData(CurrencyType type, String name, String price, String maxPrice, String minPrice, String openingPrice) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.openingPrice = openingPrice;
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

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getOpeningPrice() {
        return openingPrice;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CurrencyData && ((CurrencyData) obj).type == type;
    }
}
