package com.dropthebit.dropthebit.model;

/**
 * Created by mason-hong on 2017. 12. 14..
 * Bithumb 화폐 종류 및 키
 */
public enum BithumbType {
    BitCoin("BTC"), BitCoinCacht("BCH"), BitCoinGold("BTG"), Etherium("ETH"), EtheriumClassic("ETC"), Ripple("XRP");
    public String key;

    BithumbType(String key) {
        this.key = key;
    }
}
