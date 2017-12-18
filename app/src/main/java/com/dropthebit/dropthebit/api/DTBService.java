package com.dropthebit.dropthebit.api;

import com.dropthebit.dropthebit.dto.DTBCoinDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
public interface DTBService {
    @GET("api/coin/{currency}/{date}")
    Observable<DTBCoinDTO> getHistory(@Path("currency") String currency, @Path("date") long date);
}
