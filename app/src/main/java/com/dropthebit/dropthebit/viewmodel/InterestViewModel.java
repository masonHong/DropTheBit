package com.dropthebit.dropthebit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dropthebit.dropthebit.provider.room.InterestCoin;
import com.dropthebit.dropthebit.provider.room.InterestCoinDao;
import com.dropthebit.dropthebit.provider.room.RoomProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2018. 1. 2..
 */
public class InterestViewModel extends AndroidViewModel {
    private MutableLiveData<List<InterestCoin>> liveInterestCoins = new MutableLiveData<>();
    private ArrayList<InterestCoin> interestCoins = new ArrayList<>();
    private InterestCoinDao interestCoinDao;

    public InterestViewModel(@NonNull Application application) {
        super(application);
        interestCoinDao = RoomProvider.getInstance(application).getDatabase().interestCoinDao();
        interestCoinDao.loadAllInterestCoins()
                .subscribeOn(Schedulers.io())
                .subscribe(list -> {
                    interestCoins = new ArrayList<>(Arrays.asList(list));
                    liveInterestCoins.postValue(interestCoins);
                });
    }

    public MutableLiveData<List<InterestCoin>> getInterestCoins() {
        return liveInterestCoins;
    }

    public void addInterestCoin(String name) {
        if (find(name) == null) {
            interestCoins.add(new InterestCoin(name));
            liveInterestCoins.setValue(interestCoins);
        }
    }

    public void removeInterestCoin(String name) {
        interestCoins.remove(find(name));
        liveInterestCoins.setValue(interestCoins);
    }

    private InterestCoin find(String name) {
        for (InterestCoin coin : interestCoins) {
            if (coin.name.equals(name)) {
                return coin;
            }
        }
        return null;
    }
}
