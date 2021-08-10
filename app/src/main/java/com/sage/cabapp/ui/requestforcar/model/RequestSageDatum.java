package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 06-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RequestSageDatum {


    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("sAddress")
    @Expose
    private String sAddress;
    @SerializedName("sLat")
    @Expose
    private String sLat;
    @SerializedName("sLng")
    @Expose
    private String sLng;
    @SerializedName("dAddress")
    @Expose
    private String dAddress;
    @SerializedName("dLat")
    @Expose
    private String dLat;
    @SerializedName("dLng")
    @Expose
    private String dLng;
    @SerializedName("cAddress")
    @Expose
    private String cAddress;
    @SerializedName("cLat")
    @Expose
    private String cLat;
    @SerializedName("cLng")
    @Expose
    private String cLng;
    @SerializedName("exAddress")
    @Expose
    private String exAddress;
    @SerializedName("exLat")
    @Expose
    private String exLat;
    @SerializedName("exLng")
    @Expose
    private String exLng;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("CardType")
    @Expose
    private String cardType;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

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

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
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

    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getcLat() {
        return cLat;
    }

    public void setcLat(String cLat) {
        this.cLat = cLat;
    }

    public String getcLng() {
        return cLng;
    }

    public void setcLng(String cLng) {
        this.cLng = cLng;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    @SerializedName("devicetoken")
    @Expose
    private String devicetoken;


    @SerializedName("devicetype")
    @Expose
    private String devicetype;

}
