package com.dropthebit.dropthebit.dto;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public class BithumbAllDTO {
    private String status;
    private BithumbAllCurrencyDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BithumbAllCurrencyDTO getData() {
        return data;
    }

    public void setData(BithumbAllCurrencyDTO data) {
        this.data = data;
    }
}
