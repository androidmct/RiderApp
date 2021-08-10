package com.sage.cabapp.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 10-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class UpdateDeviceTokenRequestData {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("data")
    @Expose
    private UpdateDeviceTokenDatum data;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public UpdateDeviceTokenDatum getData() {
        return data;
    }

    public void setData(UpdateDeviceTokenDatum data) {
        this.data = data;
    }
}
