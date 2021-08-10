package com.sage.cabapp.ui.splash.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 15-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class HomeApiDatum {

    @SerializedName("userid")
    @Expose
    private String userid;

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    @SerializedName("devicetype")
    @Expose
    private String devicetype;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}