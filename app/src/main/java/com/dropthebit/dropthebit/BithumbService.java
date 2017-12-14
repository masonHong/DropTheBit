package com.dropthebit.dropthebit;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public interface BithumbService {
    @GET("public/ticker/ALL")
    Observable<BithumbDTO> getAllPrices();
}
