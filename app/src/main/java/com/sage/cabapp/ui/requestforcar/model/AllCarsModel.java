package com.sage.cabapp.ui.requestforcar.model;

/**
 * Created by Maroof Ahmed Siddique on 16-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class AllCarsModel {


    String carName = "";

    public String getCarSeats() {
        return carSeats;
    }

    public void setCarSeats(String carSeats) {
        this.carSeats = carSeats;
    }

    String carSeats = "";

    public String getCarTime() {
        return carTime;
    }

    public void setCarTime(String carTime) {
        this.carTime = carTime;
    }

    String carTime = "";

    public String getCarFare() {
        return carFare;
    }

    public void setCarFare(String carFare) {
        this.carFare = carFare;
    }

    String carFare = "";

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    String carImage = "";


    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected = false;
}
