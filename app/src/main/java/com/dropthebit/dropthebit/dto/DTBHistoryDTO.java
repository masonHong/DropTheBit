package com.dropthebit.dropthebit.dto;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
public class DTBHistoryDTO {
    private String name;
    private int price;
    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
