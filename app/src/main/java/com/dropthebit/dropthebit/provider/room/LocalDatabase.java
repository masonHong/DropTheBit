package com.dropthebit.dropthebit.provider.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
@Database(
        entities = {PriceHistory.class,InterestCoin.class,Wallet.class},
        version = 2
)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract PriceHistoryDao priceHistoryDao();
    public abstract InterestCoinDao interestCoinDao();
    public abstract WalletDao walletDao();
}
