package com.dropthebit.dropthebit.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.api.BithumbProvider;
import com.dropthebit.dropthebit.base.BaseFragment;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.dto.BithumbAllDTO;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 15..
 * MainFragment.. List?
 */
public class MainFragment extends BaseFragment {
    public int interval;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static MainFragment newInstance(int interval) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARGUMENT_INTERVAL, interval);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view) {
        interval = getArguments().getInt(Constants.ARGUMENT_INTERVAL, 2000);
        // 프래그먼트가 종료되었을 때 메모리 해제를 위해 저장하며
        // interval을 사용해서 주기적으로 호출 할 수 있도록 한다
        Disposable disposable = Observable.interval(interval, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<BithumbAllDTO>>() {
                    @Override
                    public ObservableSource<BithumbAllDTO> apply(Long aLong) throws Exception {
                        // 호출할 데이터는 Bithumb API
                        return BithumbProvider.getInstance().getAllPrices();
                    }
                })
                // io Scheduler에서 관리
                .subscribeOn(Schedulers.io())
                // 결과는 mainThread에서
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BithumbAllDTO>() {
                    @Override
                    public void accept(BithumbAllDTO bithumbAllDTO) throws Exception {
                        Log.d("Test", "Buy: " + bithumbAllDTO.getData().getBTC().getBuy_price() + ", Sell: " + bithumbAllDTO.getData().getBTC().getSell_price());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
