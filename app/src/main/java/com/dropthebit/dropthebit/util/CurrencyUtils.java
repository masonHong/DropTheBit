package com.dropthebit.dropthebit.util;

import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;

import java.util.Locale;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
public class CurrencyUtils {
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
}
