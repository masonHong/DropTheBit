package com.dropthebit.dropthebit.room;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
@Entity(
        tableName = "priceHistories",
        primaryKeys = {"name", "time"}
)
public class PriceHistory {
    @NonNull
    public String name = "";

    public long time;

    public int price;

    public PriceHistory() {
    }

    public PriceHistory(@NonNull String name, long time, int price) {
        this.name = name;
        this.time = time;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + ": " + price + " (" + time + ")";
    }
}
