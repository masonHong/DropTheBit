package com.dropthebit.dropthebit.util;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

/**
 * Created by mason-hong on 2018. 1. 1..
 */
public class RxUtils {
    public static Disposable clickOne(View view, int delay, View.OnClickListener onClickListener) {
        return RxView.clicks(view)
                .throttleFirst(delay, TimeUnit.MILLISECONDS)
                .subscribe(o -> onClickListener.onClick(view));
    }
}
