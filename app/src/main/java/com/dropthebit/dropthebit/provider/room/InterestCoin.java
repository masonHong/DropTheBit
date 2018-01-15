package com.dropthebit.dropthebit.provider.room;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


/**
 * Created by xyom on 2017-12-20.
 */

@Entity(
        tableName = "interestCoin",
        primaryKeys = "name"
)

public class InterestCoin {

    @NonNull
    public String name = "";

    public InterestCoin(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InterestCoin && ((InterestCoin) obj).name.equals(name);
    }
}
