package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 20-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CheckAcceptedRideStatusDatum {


    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("duserid")
    @Expose
    private String duserid;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDuserid() {
        return duserid;
    }

    public void setDuserid(String duserid) {
        this.duserid = duserid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }
}
