package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 20-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CheckAcceptedRideStatusMainRequest {

    @SerializedName("requestData")
    @Expose
    private CheckAcceptedRideStatusRequestData requestData;

    public CheckAcceptedRideStatusRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(CheckAcceptedRideStatusRequestData requestData) {
        this.requestData = requestData;
    }

}
