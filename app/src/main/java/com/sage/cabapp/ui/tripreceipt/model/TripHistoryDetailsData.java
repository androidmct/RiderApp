package com.sage.cabapp.ui.tripreceipt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripHistoryDetailsData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("riderId")
    @Expose
    private String riderId;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("sAddress")
    @Expose
    private String sAddress;


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
    @SerializedName("sLat")
    @Expose
    private String sLat;
    @SerializedName("dLat")
    @Expose
    private String dLat;
    @SerializedName("dLng")
    @Expose
    private String dLng;
    @SerializedName("sLng")
    @Expose
    private String sLng;
    @SerializedName("dAddress")
    @Expose
    private String dAddress;
    @SerializedName("exLat")
    @Expose
    private String exLat;
    @SerializedName("exLng")
    @Expose
    private String exLng;
    @SerializedName("exAddress")
    @Expose
    private String exAddress;
    @SerializedName("startimeFirst")
    @Expose
    private String startimeFirst;
    @SerializedName("endtimeFirst")
    @Expose
    private String endtimeFirst;
    @SerializedName("startimeSecond")
    @Expose
    private String startimeSecond;
    @SerializedName("endtimeSecond")
    @Expose
    private String endtimeSecond;
    @SerializedName("startTrip")
    @Expose
    private String startTrip;
    @SerializedName("endTrip")
    @Expose
    private String endTrip;
    @SerializedName("driverArrived")
    @Expose
    private String driverArrived;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("minimumFare")
    @Expose
    private String minimumFare;
    @SerializedName("rideTime")
    @Expose
    private String rideTime;
    @SerializedName("rideTimeTwo")
    @Expose
    private String rideTimeTwo;
    @SerializedName("rideTimeMoneyTwo")
    @Expose
    private String rideTimeMoneyTwo;
    @SerializedName("rideTimeMoney")
    @Expose
    private String rideTimeMoney;
    @SerializedName("rideDistance")
    @Expose
    private String rideDistance;
    @SerializedName("rideDistanceMoney")
    @Expose
    private String rideDistanceMoney;
    @SerializedName("tripPayment")
    @Expose
    private String tripPayment;
    @SerializedName("tripTip")
    @Expose
    private String tripTip;
    @SerializedName("tripTime")
    @Expose
    private String tripTime;
    @SerializedName("tripRating")
    @Expose
    private String tripRating;
    @SerializedName("thridParty")
    @Expose
    private String thridParty;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updateDate")
    @Expose
    private String updateDate;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSAddress() {
        return sAddress;
    }

    public void setSAddress(String sAddress) {
        this.sAddress = sAddress;
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

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
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

    public String getExAddress() {
        return exAddress;
    }

    public void setExAddress(String exAddress) {
        this.exAddress = exAddress;
    }

    public String getStartimeFirst() {
        return startimeFirst;
    }

    public void setStartimeFirst(String startimeFirst) {
        this.startimeFirst = startimeFirst;
    }

    public String getEndtimeFirst() {
        return endtimeFirst;
    }

    public void setEndtimeFirst(String endtimeFirst) {
        this.endtimeFirst = endtimeFirst;
    }

    public String getStartimeSecond() {
        return startimeSecond;
    }

    public void setStartimeSecond(String startimeSecond) {
        this.startimeSecond = startimeSecond;
    }

    public String getEndtimeSecond() {
        return endtimeSecond;
    }

    public void setEndtimeSecond(String endtimeSecond) {
        this.endtimeSecond = endtimeSecond;
    }

    public String getStartTrip() {
        return startTrip;
    }

    public void setStartTrip(String startTrip) {
        this.startTrip = startTrip;
    }

    public String getEndTrip() {
        return endTrip;
    }

    public void setEndTrip(String endTrip) {
        this.endTrip = endTrip;
    }

    public String getDriverArrived() {
        return driverArrived;
    }

    public void setDriverArrived(String driverArrived) {
        this.driverArrived = driverArrived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(String minimumFare) {
        this.minimumFare = minimumFare;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public String getRideTimeTwo() {
        return rideTimeTwo;
    }

    public void setRideTimeTwo(String rideTimeTwo) {
        this.rideTimeTwo = rideTimeTwo;
    }

    public String getRideTimeMoneyTwo() {
        return rideTimeMoneyTwo;
    }

    public void setRideTimeMoneyTwo(String rideTimeMoneyTwo) {
        this.rideTimeMoneyTwo = rideTimeMoneyTwo;
    }

    public String getRideTimeMoney() {
        return rideTimeMoney;
    }

    public void setRideTimeMoney(String rideTimeMoney) {
        this.rideTimeMoney = rideTimeMoney;
    }

    public String getRideDistance() {
        return rideDistance;
    }

    public void setRideDistance(String rideDistance) {
        this.rideDistance = rideDistance;
    }

    public String getRideDistanceMoney() {
        return rideDistanceMoney;
    }

    public void setRideDistanceMoney(String rideDistanceMoney) {
        this.rideDistanceMoney = rideDistanceMoney;
    }

    public String getTripPayment() {
        return tripPayment;
    }

    public void setTripPayment(String tripPayment) {
        this.tripPayment = tripPayment;
    }

    public String getTripTip() {
        return tripTip;
    }

    public void setTripTip(String tripTip) {
        this.tripTip = tripTip;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getTripRating() {
        return tripRating;
    }

    public void setTripRating(String tripRating) {
        this.tripRating = tripRating;
    }

    public String getThridParty() {
        return thridParty;
    }

    public void setThridParty(String thridParty) {
        this.thridParty = thridParty;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
