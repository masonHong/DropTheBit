package com.dropthebit.dropthebit;

import org.junit.Test;

import io.reactivex.functions.Consumer;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public class ApiTest {
    @Test
    public void bithumbTest() {
        BithumbProvider.getInstance()
                .getAllPrices()
                .subscribe(new Consumer<BithumbDTO>() {
                    @Override
                    public void accept(BithumbDTO bithumbDTO) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }
}
