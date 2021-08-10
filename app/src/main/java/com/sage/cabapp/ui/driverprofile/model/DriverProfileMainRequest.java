package com.sage.cabapp.ui.driverprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 24-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DriverProfileMainRequest {

    @SerializedName("requestData")
    @Expose
    private DriverProfileRequestData requestData;

    public DriverProfileRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(DriverProfileRequestData requestData) {
        this.requestData = requestData;
    }

}
