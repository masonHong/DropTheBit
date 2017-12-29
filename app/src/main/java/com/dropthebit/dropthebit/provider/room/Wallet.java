package com.dropthebit.dropthebit.provider.room;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
@Entity(
        tableName = "wallet",
        primaryKeys = "name"
)
public class Wallet {
    @NonNull
    public String name;
    public double amount;

    public Wallet(@NonNull String name, double amount) {
        this.name = name;
        this.amount = amount;
    }
}
