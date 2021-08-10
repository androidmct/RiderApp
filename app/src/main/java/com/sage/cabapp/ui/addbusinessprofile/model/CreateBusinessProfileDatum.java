package com.sage.cabapp.ui.addbusinessprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 18-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CreateBusinessProfileDatum {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("businessemail")
    @Expose
    private String businessemail;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("brand")
    @Expose
    private String brand;

    public String getDefaultActive() {
        return defaultActive;
    }

    public void setDefaultActive(String defaultActive) {
        this.defaultActive = defaultActive;
    }

    @SerializedName("defaultActive")
    @Expose
    private String defaultActive;

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

    public String getBusinessemail() {
        return businessemail;
    }

    public void setBusinessemail(String businessemail) {
        this.businessemail = businessemail;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
