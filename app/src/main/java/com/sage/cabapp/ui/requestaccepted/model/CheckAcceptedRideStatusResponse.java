package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 20-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CheckAcceptedRideStatusResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dlat")
    @Expose
    private String dlat;
    @SerializedName("dlng")
    @Expose
    private String dlng;
    @SerializedName("daddress")
    @Expose
    private String daddress;
    @SerializedName("rideStarted")
    @Expose
    private String rideStarted;
    @SerializedName("rideCompleted")
    @Expose
    private String rideCompleted;
    @SerializedName("rideCancelled")
    @Expose
    private String rideCancelled;
    @SerializedName("rideMinutes")
    @Expose
    private String rideMinutes;
    @SerializedName("minutesMessage")
    @Expose
    private String minutesMessage;

    public String getTripFare() {
        return tripFare;
    }

    public void setTripFare(String tripFare) {
        this.tripFare = tripFare;
    }

    @SerializedName("tripFare")
    @Expose
    private String tripFare;

    public String getRideCancelled() {
        return rideCancelled;
    }

    public void setRideCancelled(String rideCancelled) {
        this.rideCancelled = rideCancelled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDlat() {
        return dlat;
    }

    public void setDlat(String dlat) {
        this.dlat = dlat;
    }

    public String getDlng() {
        return dlng;
    }

    public void setDlng(String dlng) {
        this.dlng = dlng;
    }

    public String getDaddress() {
        return daddress;
    }

    public void setDaddress(String daddress) {
        this.daddress = daddress;
    }

    public String getRideStarted() {
        return rideStarted;
    }

    public void setRideStarted(String rideStarted) {
        this.rideStarted = rideStarted;
    }

    public String getRideCompleted() {
        return rideCompleted;
    }

    public void setRideCompleted(String rideCompleted) {
        this.rideCompleted = rideCompleted;
    }

    public String getRideMinutes() {
        return rideMinutes;
    }

    public void setRideMinutes(String rideMinutes) {
        this.rideMinutes = rideMinutes;
    }

    public String getMinutesMessage() {
        return minutesMessage;
    }

    public void setMinutesMessage(String minutesMessage) {
        this.minutesMessage = minutesMessage;
    }
}
