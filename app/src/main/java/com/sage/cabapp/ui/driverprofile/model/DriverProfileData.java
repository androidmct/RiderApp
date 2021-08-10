package com.sage.cabapp.ui.driverprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DriverProfileData {

    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("vehicleMake")
    @Expose
    private String vehicleMake;
    @SerializedName("vehicleModel")
    @Expose
    private String vehicleModel;
    @SerializedName("vehiclePlateNumber")
    @Expose
    private String vehiclePlateNumber;
    @SerializedName("tripComplete")
    @Expose
    private Integer tripComplete;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("driverExp")
    @Expose
    private String driverExp;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("address")
    @Expose
    private String address;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public Integer getTripComplete() {
        return tripComplete;
    }

    public void setTripComplete(Integer tripComplete) {
        this.tripComplete = tripComplete;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDriverExp() {
        return driverExp;
    }

    public void setDriverExp(String driverExp) {
        this.driverExp = driverExp;
    }

}
