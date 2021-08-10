package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 10-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RideAcceptedResponseData {

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
    @SerializedName("tdriverId")
    @Expose
    private String tdriverId;
    @SerializedName("status")
    @Expose
    private String status;

    public String getRideFare() {
        return rideFare;
    }

    public void setRideFare(String rideFare) {
        this.rideFare = rideFare;
    }

    @SerializedName("rideFare")
    @Expose
    private String rideFare;

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
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

    @SerializedName("currentStatus")
    @Expose
    private String currentStatus;
    @SerializedName("rideStatus")
    @Expose
    private String rideStatus;
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
    @SerializedName("exAddress")
    @Expose
    private String exAddress;
    @SerializedName("exLat")
    @Expose
    private String exLat;
    @SerializedName("exLng")
    @Expose
    private String exLng;
    @SerializedName("cAddress")
    @Expose
    private String cAddress;
    @SerializedName("cLat")
    @Expose
    private String cLat;
    @SerializedName("cLng")
    @Expose
    private String cLng;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("CardType")
    @Expose
    private String cardType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

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

    public String getTdriverId() {
        return tdriverId;
    }

    public void setTdriverId(String tdriverId) {
        this.tdriverId = tdriverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSAddress() {
        return sAddress;
    }

    public void setSAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getSLat() {
        return sLat;
    }

    public void setSLat(String sLat) {
        this.sLat = sLat;
    }

    public String getSLng() {
        return sLng;
    }

    public void setSLng(String sLng) {
        this.sLng = sLng;
    }

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
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

    public String getCAddress() {
        return cAddress;
    }

    public void setCAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getCLat() {
        return cLat;
    }

    public void setCLat(String cLat) {
        this.cLat = cLat;
    }

    public String getCLng() {
        return cLng;
    }

    public void setCLng(String cLng) {
        this.cLng = cLng;
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


}
