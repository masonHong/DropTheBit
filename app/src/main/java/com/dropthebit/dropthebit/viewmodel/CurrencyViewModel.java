package com.dropthebit.dropthebit.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.dropthebit.dropthebit.api.BithumbProvider;
import com.dropthebit.dropthebit.dto.BithumbCurrencyDTO;
import com.dropthebit.dropthebit.model.CurrencyData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 17..
 */
public class CurrencyViewModel extends ViewModel {
    private MutableLiveData<List<CurrencyData>> currencyList = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> BTC = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> BCH = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> BTG = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> ETH = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> ETC = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> XRP = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> QTUM = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> LTC = new MutableLiveData<>();
    private MutableLiveData<CurrencyData> DASH = new MutableLiveData<>();

    private Disposable disposableTotal = null;

    public CurrencyViewModel() {
        super();
        // 프래그먼트가 종료되었을 때 메모리 해제를 위해 저장하며
        // interval을 사용해서 주기적으로 호출 할 수 있도록 한다
        disposableTotal = Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
                .flatMap(aLong -> {
                    // 호출할 데이터는 Bithumb API
                    return BithumbProvider.getInstance().getAllPrices();
                })
                .map(dto -> {
                    List<CurrencyData> ret = new ArrayList<>();
                    BithumbCurrencyDTO target = dto.getData().getBTC();
                    ret.add(new CurrencyData("비트코인", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getBCH();
                    ret.add(new CurrencyData("비트코인캐시", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getBTG();
                    ret.add(new CurrencyData("비트코인골드", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getETH();
                    ret.add(new CurrencyData("이더리움", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getETC();
                    ret.add(new CurrencyData("이더리움클래식", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getXRP();
                    ret.add(new CurrencyData("리플", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getQTUM();
                    ret.add(new CurrencyData("퀀텀", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getLTC();
                    ret.add(new CurrencyData("라이트코인", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getDASH();
                    ret.add(new CurrencyData("대쉬", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    return ret;
                })
                // io Scheduler에서 관리
                .subscribeOn(Schedulers.io())
                // 결과는 mainThread에서
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    currencyList.setValue(list);
                    BTC.setValue(list.get(0));
                    BCH.setValue(list.get(1));
                    BTG.setValue(list.get(2));
                    ETH.setValue(list.get(3));
                    ETC.setValue(list.get(4));
                    XRP.setValue(list.get(5));
                    QTUM.setValue(list.get(6));
                    LTC.setValue(list.get(7));
                    DASH.setValue(list.get(8));
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposableTotal != null) {
            disposableTotal.dispose();
        }
    }

    public MutableLiveData<List<CurrencyData>> getCurrencyList() {
        return currencyList;
    }

    public MutableLiveData<CurrencyData> getBTC() {
        return BTC;
    }

    public MutableLiveData<CurrencyData> getBCH() {
        return BCH;
    }

    public MutableLiveData<CurrencyData> getBTG() {
        return BTG;
    }

    public MutableLiveData<CurrencyData> getETH() {
        return ETH;
    }

    public MutableLiveData<CurrencyData> getETC() {
        return ETC;
    }

    public MutableLiveData<CurrencyData> getXRP() {
        return XRP;
    }

    public MutableLiveData<CurrencyData> getQTUM() {
        return QTUM;
    }

    public MutableLiveData<CurrencyData> getLTC() {
        return LTC;
    }

    public MutableLiveData<CurrencyData> getDASH() {
        return DASH;
    }
}
