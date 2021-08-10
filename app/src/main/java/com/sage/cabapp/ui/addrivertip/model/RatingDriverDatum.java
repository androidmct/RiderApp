package com.sage.cabapp.ui.addrivertip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RatingDriverDatum {


    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("tripRating")
    @Expose
    private String tripRating;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("tripTip")
    @Expose
    private String tripTip;

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

    public String getTripRating() {
        return tripRating;
    }

    public void setTripRating(String tripRating) {
        this.tripRating = tripRating;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTripTip() {
        return tripTip;
    }

    public void setTripTip(String tripTip) {
        this.tripTip = tripTip;
    }
}
