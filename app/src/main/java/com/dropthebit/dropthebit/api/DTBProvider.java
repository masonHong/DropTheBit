package com.dropthebit.dropthebit.api;

import com.dropthebit.dropthebit.dto.DTBCoinDTO;
import com.dropthebit.dropthebit.model.CurrencyType;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mason-hong on 2017. 12. 18..
 */


public class DTBProvider {
    private static final String BASE_URL = "http://52.79.231.100:5252/";
    private static DTBProvider instance;
    private DTBService service;

    public static DTBProvider getInstance() {
        if (instance == null) {
            synchronized (DTBProvider.class) {
                if (instance == null) {
                    instance = new DTBProvider();
                }
            }
        }
        return instance;
    }

    private DTBProvider() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(DTBService.class);
    }

    public Flowable<DTBCoinDTO> getHistory(CurrencyType type, long date) {
        return service.getHistory(type.key, date);
    }
}
