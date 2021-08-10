package com.sage.cabapp.ui.howtopay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 28-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class PaypalTokenMainRequest {


    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("paypaltoken")
    @Expose
    private String paypaltoken;
    @SerializedName("apikey")
    @Expose
    private String apikey;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getPaypaltoken() {
        return paypaltoken;
    }

    public void setPaypaltoken(String paypaltoken) {
        this.paypaltoken = paypaltoken;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
