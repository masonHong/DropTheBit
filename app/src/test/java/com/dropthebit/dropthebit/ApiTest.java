package com.dropthebit.dropthebit;

import com.dropthebit.dropthebit.api.BithumbProvider;
import com.dropthebit.dropthebit.dto.BithumbAllDTO;
import com.dropthebit.dropthebit.dto.BithumbOneDTO;
import com.dropthebit.dropthebit.model.BithumbType;

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
                .subscribe(new Consumer<BithumbAllDTO>() {
                    @Override
                    public void accept(BithumbAllDTO bithumbDTO) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });

        BithumbProvider.getInstance()
                .getPrice(BithumbType.BitCoin)
                .subscribe(new Consumer<BithumbOneDTO>() {
                    @Override
                    public void accept(BithumbOneDTO bithumbDTO) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }
}
