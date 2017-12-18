package com.dropthebit.dropthebit.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
@Dao
public interface PriceHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPriceHistories(PriceHistory... priceHistories);

    @Query("SELECT * FROM priceHistories WHERE :name")
    PriceHistory[] loadAllHistories(String name);
}
