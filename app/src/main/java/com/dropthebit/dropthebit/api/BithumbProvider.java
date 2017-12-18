package com.dropthebit.dropthebit.api;

import com.dropthebit.dropthebit.dto.BithumbAllDTO;
import com.dropthebit.dropthebit.dto.BithumbOneDTO;
import com.dropthebit.dropthebit.model.CurrencyType;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mason-hong on 2017. 12. 14..
 * Bithumb API 제공자
 */
public class BithumbProvider {
    private static final String BASE_URL = "https://api.bithumb.com/";
    private static BithumbProvider instance;
    private BithumbService service;

    public static BithumbProvider getInstance() {
        if (instance == null) {
            synchronized (BithumbProvider.class) {
                if (instance == null) {
                    instance = new BithumbProvider();
                }
            }
        }
        return instance;
    }

    private BithumbProvider() {
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
        service = retrofit.create(BithumbService.class);
    }

    public Observable<BithumbAllDTO> getAllPrices() {
        return service.getAllPrices();
    }

    public Observable<BithumbOneDTO> getPrice(CurrencyType type) {
        return service.getPrice(type.key);
    }
}
