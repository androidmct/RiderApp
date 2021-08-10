package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 31-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetAllCarsData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("carType")
    @Expose
    private String carType;
    @SerializedName("seats")
    @Expose
    private String seats;
    @SerializedName("carImage")
    @Expose
    private String carImage;
    @SerializedName("carSpeed")
    @Expose
    private String carSpeed;
    @SerializedName("carFee")
    @Expose
    private String carFee;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("timeToTravel")
    @Expose
    private String timeToTravel;
    @SerializedName("estimateFee")
    @Expose
    private String estimateFee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getCarSpeed() {
        return carSpeed;
    }

    public void setCarSpeed(String carSpeed) {
        this.carSpeed = carSpeed;
    }

    public String getCarFee() {
        return carFee;
    }

    public void setCarFee(String carFee) {
        this.carFee = carFee;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getTimeToTravel() {
        return timeToTravel;
    }

    public void setTimeToTravel(String timeToTravel) {
        this.timeToTravel = timeToTravel;
    }

    public String getEstimateFee() {
        return estimateFee;
    }

    public void setEstimateFee(String estimateFee) {
        this.estimateFee = estimateFee;
    }
}
