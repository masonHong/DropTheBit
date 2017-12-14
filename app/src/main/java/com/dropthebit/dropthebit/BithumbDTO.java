package com.dropthebit.dropthebit;

/**
 * Created by mason-hong on 2017. 12. 14..
 */
public class BithumbDTO {
    private String status;
    private AllCurrencyDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AllCurrencyDTO getData() {
        return data;
    }

    public void setData(AllCurrencyDTO data) {
        this.data = data;
    }
}
