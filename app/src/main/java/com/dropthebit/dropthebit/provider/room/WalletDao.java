package com.dropthebit.dropthebit.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import io.reactivex.Maybe;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
@Dao
public interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWallet(Wallet wallet);

    @Query("SELECT * FROM wallet")
    Maybe<Wallet[]> loadAllWallet();

    @Query("SELECT * FROM wallet WHERE name=:name")
    Maybe<Wallet> loadWallet(String name);

    @Update
    void updateWallet(Wallet wallet);
}
