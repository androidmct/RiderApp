package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 31-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetAllCarsDatum {

    @SerializedName("sLat")
    @Expose
    private String sLat;
    @SerializedName("sLng")
    @Expose
    private String sLng;
    @SerializedName("dLat")
    @Expose
    private String dLat;
    @SerializedName("dLng")
    @Expose
    private String dLng;
    @SerializedName("exLat")
    @Expose
    private String exLat;

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

    @SerializedName("exLng")
    @Expose
    private String exLng;

    public String getsLat() {
        return sLat;
    }

    public void setsLat(String sLat) {
        this.sLat = sLat;
    }

    public String getsLng() {
        return sLng;
    }

    public void setsLng(String sLng) {
        this.sLng = sLng;
    }

    public String getdLat() {
        return dLat;
    }

    public void setdLat(String dLat) {
        this.dLat = dLat;
    }

    public String getdLng() {
        return dLng;
    }

    public void setdLng(String dLng) {
        this.dLng = dLng;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    @SerializedName("devicetype")
    @Expose
    private String devicetype;

}
