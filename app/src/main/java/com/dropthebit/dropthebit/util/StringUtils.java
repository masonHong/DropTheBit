package com.dropthebit.dropthebit.util;

import java.text.NumberFormat;

/**
 * Created by mason-hong on 2018. 1. 7..
 */
public class StringUtils {
    public static String getPriceString(long number, String unit) {
        String ret = NumberFormat.getIntegerInstance().format(number);
        if (!isEmpty(unit)) {
            ret += " " + unit;
        }
        return ret;
    }

    public static String getPriceString(double number, String unit) {
        return getPriceString((long) number, unit);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
