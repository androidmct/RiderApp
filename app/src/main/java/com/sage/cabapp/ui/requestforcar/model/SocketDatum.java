package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 15-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class SocketDatum {

    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("rider_id")
    @Expose
    private String riderId;
    @SerializedName("status")
    @Expose
    private String status;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @SerializedName("deviceType")
    @Expose
    private String deviceType;

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    @SerializedName("rider_name")
    @Expose
    private String rider_name;

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

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
