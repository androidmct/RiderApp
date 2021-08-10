package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 09-06-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class RideAcceptedVehicleData {

    @SerializedName("vehicleYear")
    @Expose
    private String vehicleYear;

    @SerializedName("vehicleMake")
    @Expose
    private String vehicleMake;

    @SerializedName("vehicleModel")
    @Expose
    private String vehicleModel;

    @SerializedName("vehiclePlateNumber")
    @Expose
    private String vehiclePlateNumber;

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    @SerializedName("driverRating")
    @Expose
    private String driverRating;
}
