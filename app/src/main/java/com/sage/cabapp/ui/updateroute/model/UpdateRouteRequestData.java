package com.sage.cabapp.ui.updateroute.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 23-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class UpdateRouteRequestData {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("data")
    @Expose
    private UpdateRouteDatum data;

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

    public UpdateRouteDatum getData() {
        return data;
    }

    public void setData(UpdateRouteDatum data) {
        this.data = data;
    }
}
