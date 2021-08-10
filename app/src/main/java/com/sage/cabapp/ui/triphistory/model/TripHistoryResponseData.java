package com.sage.cabapp.ui.triphistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripHistoryResponseData implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("rideFare")
    @Expose
    private String rideFare;
    @SerializedName("CardType")
    @Expose
    private String cardType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public float getTripRating() {
        return tripRating;
    }

    public void setTripRating(float tripRating) {
        this.tripRating = tripRating;
    }

    @SerializedName("tripRating")
    @Expose
    private float tripRating;
    @SerializedName("driverData")
    @Expose
    private List<TripHistoryDriverData> driverData = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRideFare() {
        return rideFare;
    }

    public void setRideFare(String rideFare) {
        this.rideFare = rideFare;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<TripHistoryDriverData> getDriverData() {
        return driverData;
    }

    public void setDriverData(List<TripHistoryDriverData> driverData) {
        this.driverData = driverData;
    }

}
