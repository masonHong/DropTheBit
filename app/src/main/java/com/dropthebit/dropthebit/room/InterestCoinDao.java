package com.dropthebit.dropthebit.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;

/**
 * Created by xyom on 2017-12-20.
 */

@Dao
public interface InterestCoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntersetCoin(InterestCoin interestCoin);

    @Query("select * from intersetCoin")
    Flowable<InterestCoin[]> loadAllInterestCoins();

    @Delete
    void deleteCoin(InterestCoin interestCoin);
}
