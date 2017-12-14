package com.dropthebit.dropthebit;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public enum BithumbType {
    BitCoin("BTC"), BitCoinCacht("BCH"), BitCoinGold("BTG"), Etherium("ETH"), EtheriumClassic("ETC"), Ripple("XRP");
    String key;

    BithumbType(String key) {
        this.key = key;
    }
}
