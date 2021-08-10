package com.sage.cabapp.ui.addrivertip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RatingDriverMainRequest {

    @SerializedName("requestData")
    @Expose
    private RatingDriverRequestData requestData;

    public RatingDriverRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RatingDriverRequestData requestData) {
        this.requestData = requestData;
    }
}
