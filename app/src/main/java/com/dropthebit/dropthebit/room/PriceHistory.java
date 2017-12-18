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
}
