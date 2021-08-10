package com.sage.cabapp.ui.tipthemmore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TipThemDatum {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("tripTip")
    @Expose
    private String tripTip;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("driverId")
    @Expose
    private String driverId;

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

    public String getTripTip() {
        return tripTip;
    }

    public void setTripTip(String tripTip) {
        this.tripTip = tripTip;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

}
