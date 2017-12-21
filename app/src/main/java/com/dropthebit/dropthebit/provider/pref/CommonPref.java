package com.dropthebit.dropthebit.provider.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mason-hong on 2017. 12. 21..
 */
public class CommonPref {
    private static final String PREF_NAME = "common";
    private static final String FIELD_FIRST_START = "firstStart";
    private static CommonPref instance;
    private SharedPreferences pref;

    public static CommonPref getInstance(Context context) {
        if (instance == null) {
            synchronized (CommonPref.class) {
                if (instance == null) {
                    instance = new CommonPref(context);
                }
            }
        }
        return instance;
    }

    private CommonPref(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setFirstStart(boolean firstStart) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FIELD_FIRST_START, firstStart);
        editor.apply();
    }

    public boolean isFirstStart() {
        return pref.getBoolean(FIELD_FIRST_START, true);
    }
}
