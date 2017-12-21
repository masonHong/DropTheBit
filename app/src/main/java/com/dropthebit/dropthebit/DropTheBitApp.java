package com.dropthebit.dropthebit;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by mason-hong on 2017. 12. 21..
 */
public class DropTheBitApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
