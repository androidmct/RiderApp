package com.sage.cabapp.ui.splash.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 15-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class HomeApiMainRequest {

    @SerializedName("requestData")
    @Expose
    private HomeApiRequestData requestData;

    public HomeApiRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(HomeApiRequestData requestData) {
        this.requestData = requestData;
    }
}
