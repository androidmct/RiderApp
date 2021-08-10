package com.sage.cabapp.ui.requestforcar.model;

/**
 * Created by Maroof Ahmed Siddique on 29-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class NearestDriverDummy {

    public NearestDriverDummy(String driverID, double distance) {
        this.driverID = driverID;
        this.distance = distance;
    }

    private String driverID;

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;

}
