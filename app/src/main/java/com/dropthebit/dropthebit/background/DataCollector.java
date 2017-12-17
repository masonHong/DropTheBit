package com.dropthebit.dropthebit.background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by xyom on 2017-12-17.
 */

public class DataCollector extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return super.onStartCommand(intent, flags, startId);
    }
}
