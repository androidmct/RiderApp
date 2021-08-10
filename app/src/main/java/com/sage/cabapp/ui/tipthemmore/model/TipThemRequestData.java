package com.sage.cabapp.ui.tipthemmore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TipThemRequestData {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("data")
    @Expose
    private TipThemDatum data;

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

    public TipThemDatum getData() {
        return data;
    }

    public void setData(TipThemDatum data) {
        this.data = data;
    }
}