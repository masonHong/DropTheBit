package com.dropthebit.dropthebit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.api.BithumbProvider;
import com.dropthebit.dropthebit.dto.BithumbAllCurrencyDTO;
import com.dropthebit.dropthebit.dto.BithumbAllDTO;
import com.dropthebit.dropthebit.dto.BithumbCurrencyDTO;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 17..
 */
public class CurrencyViewModel extends AndroidViewModel {
    private MutableLiveData<LinkedHashMap<String, CurrencyData>> currencyList = new MutableLiveData<>();
    private String[] coinNames;

    private Disposable disposableTotal = null;

    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        // 프래그먼트가 종료되었을 때 메모리 해제를 위해 저장하며
        // interval을 사용해서 주기적으로 호출 할 수 있도록 한다

        coinNames = application.getResources().getStringArray(R.array.coinNames);
        disposableTotal = Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
                .flatMap(aLong -> {
                    // 호출할 데이터는 Bithumb API
                    return BithumbProvider.getInstance().getAllPrices();
                })
                .map(BithumbAllDTO::getData)
                // io Scheduler에서 관리
                .subscribeOn(Schedulers.io())
                // 결과는 mainThread에서
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    LinkedHashMap<String, CurrencyData> map = new LinkedHashMap<>();
                    BithumbCurrencyDTO target = data.getBTC();
                    map.put(CurrencyType.BitCoin.key, new CurrencyData(CurrencyType.BitCoin, coinNames[0], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getBCH();
                    map.put(CurrencyType.BitCoinCache.key, new CurrencyData(CurrencyType.BitCoinCache, coinNames[1], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getBTG();
                    map.put(CurrencyType.BitCoinGold.key, new CurrencyData(CurrencyType.BitCoinGold, coinNames[2], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getETH();
                    map.put(CurrencyType.Etherium.key, new CurrencyData(CurrencyType.Etherium, coinNames[3], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getBTC();
                    map.put(CurrencyType.EtheriumClassic.key, new CurrencyData(CurrencyType.EtheriumClassic, coinNames[4], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getXRP();
                    map.put(CurrencyType.Ripple.key, new CurrencyData(CurrencyType.Ripple, coinNames[5], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getLTC();
                    map.put(CurrencyType.LiteCoin.key, new CurrencyData(CurrencyType.LiteCoin, coinNames[6], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getQTUM();
                    map.put(CurrencyType.Qtum.key, new CurrencyData(CurrencyType.Qtum, coinNames[7], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = data.getDASH();
                    map.put(CurrencyType.Dash.key, new CurrencyData(CurrencyType.Dash, coinNames[8], target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    currencyList.setValue(map);
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposableTotal != null) {
            disposableTotal.dispose();
        }
    }

    public MutableLiveData<LinkedHashMap<String, CurrencyData>> getCurrencyList() {
        return currencyList;
    }
}
