package com.dropthebit.dropthebit.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by HTJ_Home_PC on 2018-01-10.
 */

public class AndroidUtils {
    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
