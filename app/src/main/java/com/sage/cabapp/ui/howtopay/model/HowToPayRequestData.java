package com.sage.cabapp.ui.howtopay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class HowToPayRequestData {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("data")
    @Expose
    private HowToPayDatum data;

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

    public HowToPayDatum getData() {
        return data;
    }

    public void setData(HowToPayDatum data) {
        this.data = data;
    }
}
