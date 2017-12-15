package com.dropthebit.dropthebit.api.provider;

import com.dropthebit.dropthebit.api.service.KorbitService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mason-hong on 2017. 12. 14..
 * Korbit api 제공자
 */
public class KorbitProvider {
    private static final String BASE_URL = "https://api.bithumb.com/";
    private static KorbitProvider instance;
    private KorbitService service;

    public static KorbitProvider getInstance() {
        if (instance == null) {
            synchronized (KorbitProvider.class) {
                if (instance == null) {
                    instance = new KorbitProvider();
                }
            }
        }
        return instance;
    }

    private KorbitProvider() {
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
        service = retrofit.create(KorbitService.class);
    }
}
