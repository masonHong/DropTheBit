package com.dropthebit.dropthebit.dto;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public class BithumbCurrencyDTO {
    private String opening_price;
    private String closing_price;
    private String min_price;
    private String max_price;
    private long date;

    public String getOpening_price() {
        return opening_price;
    }

    public void setOpening_price(String opening_price) {
        this.opening_price = opening_price;
    }

    public String getClosing_price() {
        return closing_price;
    }

    public void setClosing_price(String closing_price) {
        this.closing_price = closing_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
