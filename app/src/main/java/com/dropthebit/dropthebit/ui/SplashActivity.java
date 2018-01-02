package com.dropthebit.dropthebit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.pref.CommonPref;
import com.dropthebit.dropthebit.provider.room.InterestCoin;
import com.dropthebit.dropthebit.provider.room.RoomProvider;
import com.dropthebit.dropthebit.ui.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (CommonPref.getInstance(this).isFirstStart()) {
            Disposable disposable = Observable.just(RoomProvider.getInstance(this).getDatabase().interestCoinDao())
                    .subscribeOn(Schedulers.io())
                    .subscribe(dao -> {
                        dao.insertInterestCoin(new InterestCoin(CurrencyType.BitCoin.key));
                        dao.insertInterestCoin(new InterestCoin(CurrencyType.Etherium.key));
                        dao.insertInterestCoin(new InterestCoin(CurrencyType.Ripple.key));
                        CommonPref.getInstance(this).setFirstStart(false);
                    }, Throwable::printStackTrace);
            compositeDisposable.add(disposable);
        }

        Disposable disposable = Observable.just(0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    finish();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 백 버튼 시 액티비티 시작 취소
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
