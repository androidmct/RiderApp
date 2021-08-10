package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 10-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RideAcceptedMainRequest {

    @SerializedName("requestData")
    @Expose
    private RideAcceptedRequestData requestData;

    public RideAcceptedRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RideAcceptedRequestData requestData) {
        this.requestData = requestData;
    }
}
