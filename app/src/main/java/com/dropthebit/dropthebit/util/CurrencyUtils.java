package com.dropthebit.dropthebit.util;

import android.support.annotation.Nullable;

import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
public class CurrencyUtils {
    private static HashMap<CurrencyType, String> coinImageMap;

    static {
        coinImageMap = new HashMap<>();
        coinImageMap.put(CurrencyType.BitCoin, "https://static.upbit.com/logos/BTC.png");
        coinImageMap.put(CurrencyType.BitCoinCache, "https://static.upbit.com/logos/BCC.png");
        coinImageMap.put(CurrencyType.BitCoinGold, "https://static.upbit.com/logos/BTG.png");
        coinImageMap.put(CurrencyType.Etherium, "https://static.upbit.com/logos/ETH.png");
        coinImageMap.put(CurrencyType.Ripple, "https://static.upbit.com/logos/XRP.png");
        coinImageMap.put(CurrencyType.EtheriumClassic, "https://static.upbit.com/logos/ETC.png");
        coinImageMap.put(CurrencyType.LiteCoin, "https://static.upbit.com/logos/LTC.png");
        coinImageMap.put(CurrencyType.Qtum, "https://static.upbit.com/logos/QTUM.png");
        coinImageMap.put(CurrencyType.Dash, "https://static.upbit.com/logos/DASH.png");
    }

    public static CurrencyType findByName(String name) {
        for (CurrencyType currencyType : CurrencyType.values()) {
            if (currencyType.key.equals(name)) {
                return currencyType;
            }
        }
        return null;
    }

    public static long getSafetyPrice(CurrencyData data) {
        return getSafetyPrice(data.getPrice());
    }

    public static long getSafetyPrice(String price) {
        if (price.contains(".")) {
            price = price.substring(0, price.indexOf("."));
        }
        return Long.parseLong(price);
    }

    public static String getDiffString(CurrencyData data) {
        long openingPrice = getSafetyPrice(data.getOpeningPrice());
        long currentPrice = getSafetyPrice(data.getPrice());
        long diffPrice = currentPrice - openingPrice;
        double diffPercent = (diffPrice / (double) openingPrice) * 100;
        return String.format(Locale.getDefault(), (diffPrice >= 0 ? "+" : "") + "%s ( %.02f%% )", StringUtils.getPriceString(diffPrice, ""), diffPercent);
    }

    @Nullable
    public static String getCoinImage(CurrencyType type) {
        return coinImageMap.get(type);
    }
}
