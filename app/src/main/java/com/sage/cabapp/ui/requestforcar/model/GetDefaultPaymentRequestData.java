package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 18-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetDefaultPaymentRequestData {


    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("data")
    @Expose
    private GetDefaultPaymentDatum data;

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

    public GetDefaultPaymentDatum getData() {
        return data;
    }

    public void setData(GetDefaultPaymentDatum data) {
        this.data = data;
    }
}
