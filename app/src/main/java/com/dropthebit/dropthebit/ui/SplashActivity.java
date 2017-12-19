package com.dropthebit.dropthebit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.ui.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends AppCompatActivity {

    private Disposable disposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        disposable = Observable.just(0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        finish();
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 백 버튼 시 액티비티 시작 취소
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
