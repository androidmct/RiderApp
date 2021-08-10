package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 10-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CancelSageDatum {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("rideStatus")
    @Expose
    private String rideSatus;

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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRideSatus() {
        return rideSatus;
    }

    public void setRideSatus(String rideSatus) {
        this.rideSatus = rideSatus;
    }

}
