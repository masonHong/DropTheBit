package com.dropthebit.dropthebit.util;

import com.dropthebit.dropthebit.model.CurrencyType;

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
}
