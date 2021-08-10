package com.sage.cabapp.ui.tripreceipt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripHistoryDetailsMainRequest {

    @SerializedName("requestData")
    @Expose
    private TripHistoryDetailsRequestData requestData;

    public TripHistoryDetailsRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(TripHistoryDetailsRequestData requestData) {
        this.requestData = requestData;
    }
}
