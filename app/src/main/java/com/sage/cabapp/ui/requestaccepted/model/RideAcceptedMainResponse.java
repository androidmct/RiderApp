package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 10-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RideAcceptedMainResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestData")
    @Expose
    private RideAcceptedResponseData requestData;
    @SerializedName("driverData")
    @Expose
    private RideAcceptedDriverData driverData;

    public RideAcceptedVehicleData getDriverVehicle() {
        return driverVehicle;
    }

    public void setDriverVehicle(RideAcceptedVehicleData driverVehicle) {
        this.driverVehicle = driverVehicle;
    }

    @SerializedName("driverVehicle")
    @Expose
    private RideAcceptedVehicleData driverVehicle;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RideAcceptedResponseData getRequestData() {
        return requestData;
    }

    public void setRequestData(RideAcceptedResponseData requestData) {
        this.requestData = requestData;
    }

    public RideAcceptedDriverData getDriverData() {
        return driverData;
    }

    public void setDriverData(RideAcceptedDriverData driverData) {
        this.driverData = driverData;
    }


}
