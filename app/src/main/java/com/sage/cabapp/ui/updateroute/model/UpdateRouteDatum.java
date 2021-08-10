package com.sage.cabapp.ui.updateroute.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 23-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class UpdateRouteDatum {

    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("dAddress")
    @Expose
    private String dAddress;
    @SerializedName("dLat")
    @Expose
    private String dLat;
    @SerializedName("dLng")
    @Expose
    private String dLng;
    @SerializedName("exAddress")
    @Expose
    private String exAddress;
    @SerializedName("exLat")
    @Expose
    private String exLat;
    @SerializedName("exLng")
    @Expose
    private String exLng;

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getDLat() {
        return dLat;
    }

    public void setDLat(String dLat) {
        this.dLat = dLat;
    }

    public String getDLng() {
        return dLng;
    }

    public void setDLng(String dLng) {
        this.dLng = dLng;
    }

    public String getExAddress() {
        return exAddress;
    }

    public void setExAddress(String exAddress) {
        this.exAddress = exAddress;
    }

    public String getExLat() {
        return exLat;
    }

    public void setExLat(String exLat) {
        this.exLat = exLat;
    }

    public String getExLng() {
        return exLng;
    }

    public void setExLng(String exLng) {
        this.exLng = exLng;
    }
}
