package com.dropthebit.dropthebit.room;

import android.arch.persistence.room.Entity;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
@Entity(
        tableName = "priceHistories",
        primaryKeys = {"name", "time"}
)
public class PriceHistory {
    public String name;
    public long time;
    public int price;
}
