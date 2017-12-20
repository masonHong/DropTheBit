package com.dropthebit.dropthebit.room;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


/**
 * Created by xyom on 2017-12-20.
 */

@Entity(
        tableName = "intersetCoin",
        primaryKeys = "name"
)

public class InterestCoin {

    @NonNull
    public String name="";

    public InterestCoin()
    {

    }

    public  InterestCoin(@NonNull String name)
    {
        this.name = name;
    }

    @Override
    public String toString(){return name;}
}
