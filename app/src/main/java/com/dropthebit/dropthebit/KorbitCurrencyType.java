package com.dropthebit.dropthebit;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public enum KorbitCurrencyType {
    BitCoin("btc_krw"), BitCoinCacht("bch_krw"), BitCoinGold("btg_krw"), Etherium("eth_krw"), EtheriumClassic("etc_krw"), Ripple("xrp_krw");
    String key;

    KorbitCurrencyType(String key) {
        this.key = key;
    }
}
