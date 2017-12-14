package com.dropthebit.dropthebit.dto;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public class BithumbOneDTO {
    private String status;
    private BithumbCurrencyDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BithumbCurrencyDTO getData() {
        return data;
    }

    public void setData(BithumbCurrencyDTO data) {
        this.data = data;
    }
}
