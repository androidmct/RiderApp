package com.sage.cabapp.ui.tripfare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ConstantAPIData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("minDistance")
    @Expose
    private String minDistance;
    @SerializedName("maxDistance")
    @Expose
    private String maxDistance;
    @SerializedName("driverFind")
    @Expose
    private String driverFind;
    @SerializedName("limitDriver")
    @Expose
    private String limitDriver;
    @SerializedName("ridercanceltimeoutminutes")
    @Expose
    private String ridercanceltimeoutminutes;
    @SerializedName("driverdeclinetimeoutseconds")
    @Expose
    private String driverdeclinetimeoutseconds;
    @SerializedName("maxrequesttimeoutminutes")
    @Expose
    private String maxrequesttimeoutminutes;
    @SerializedName("minimumFare")
    @Expose
    private String minimumFare;
    @SerializedName("fareTimeMoney")
    @Expose
    private String fareTimeMoney;
    @SerializedName("fareMilesMoney")
    @Expose
    private String fareMilesMoney;
    @SerializedName("thridPartyFee")
    @Expose
    private String thridPartyFee;
    @SerializedName("adminLat")
    @Expose
    private String adminLat;
    @SerializedName("adminLng")
    @Expose
    private String adminLng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(String minDistance) {
        this.minDistance = minDistance;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getDriverFind() {
        return driverFind;
    }

    public void setDriverFind(String driverFind) {
        this.driverFind = driverFind;
    }

    public String getLimitDriver() {
        return limitDriver;
    }

    public void setLimitDriver(String limitDriver) {
        this.limitDriver = limitDriver;
    }

    public String getRidercanceltimeoutminutes() {
        return ridercanceltimeoutminutes;
    }

    public void setRidercanceltimeoutminutes(String ridercanceltimeoutminutes) {
        this.ridercanceltimeoutminutes = ridercanceltimeoutminutes;
    }

    public String getDriverdeclinetimeoutseconds() {
        return driverdeclinetimeoutseconds;
    }

    public void setDriverdeclinetimeoutseconds(String driverdeclinetimeoutseconds) {
        this.driverdeclinetimeoutseconds = driverdeclinetimeoutseconds;
    }

    public String getMaxrequesttimeoutminutes() {
        return maxrequesttimeoutminutes;
    }

    public void setMaxrequesttimeoutminutes(String maxrequesttimeoutminutes) {
        this.maxrequesttimeoutminutes = maxrequesttimeoutminutes;
    }

    public String getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(String minimumFare) {
        this.minimumFare = minimumFare;
    }

    public String getFareTimeMoney() {
        return fareTimeMoney;
    }

    public void setFareTimeMoney(String fareTimeMoney) {
        this.fareTimeMoney = fareTimeMoney;
    }

    public String getFareMilesMoney() {
        return fareMilesMoney;
    }

    public void setFareMilesMoney(String fareMilesMoney) {
        this.fareMilesMoney = fareMilesMoney;
    }

    public String getThridPartyFee() {
        return thridPartyFee;
    }

    public void setThridPartyFee(String thridPartyFee) {
        this.thridPartyFee = thridPartyFee;
    }

    public String getAdminLat() {
        return adminLat;
    }

    public void setAdminLat(String adminLat) {
        this.adminLat = adminLat;
    }

    public String getAdminLng() {
        return adminLng;
    }

    public void setAdminLng(String adminLng) {
        this.adminLng = adminLng;
    }
}
