package com.sage.cabapp.ui.triphistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripHistoryMainRequest {

    @SerializedName("requestData")
    @Expose
    private TripHistoryRequestData requestData;

    public TripHistoryRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(TripHistoryRequestData requestData) {
        this.requestData = requestData;
    }

}
