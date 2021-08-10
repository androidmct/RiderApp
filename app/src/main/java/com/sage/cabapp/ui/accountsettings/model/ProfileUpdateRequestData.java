package com.sage.cabapp.ui.accountsettings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 20-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ProfileUpdateRequestData {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("data")
    @Expose
    private ProfileUpdateDatum data;

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

    public ProfileUpdateDatum getData() {
        return data;
    }

    public void setData(ProfileUpdateDatum data) {
        this.data = data;
    }

}
