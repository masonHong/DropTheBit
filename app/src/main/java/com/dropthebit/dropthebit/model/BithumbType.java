package com.dropthebit.dropthebit.model;

/**
 * Created by mason-hong on 2017. 12. 14..
 * Bithumb 화폐 종류 및 키
 */
public enum BithumbType {
    BitCoin("BTC"),
    BitCoinCache("BCH"),
    BitCoinGold("BTG"),
    Etherium("ETH"),
    EtheriumClassic("ETC"),
    Ripple("XRP"),
    Qtum("QTUM"),
    LiteCoin("LTC"),
    Dash("DASH");

    public String key;

    BithumbType(String key) {
        this.key = key;
    }
}
