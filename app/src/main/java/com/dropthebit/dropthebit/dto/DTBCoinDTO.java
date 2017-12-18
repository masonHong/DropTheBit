package com.dropthebit.dropthebit.dto;

import java.util.List;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
public class DTBCoinDTO {
    private String status;
    private List<DTBHistoryDTO> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DTBHistoryDTO> getData() {
        return data;
    }

    public void setData(List<DTBHistoryDTO> data) {
        this.data = data;
    }
}
