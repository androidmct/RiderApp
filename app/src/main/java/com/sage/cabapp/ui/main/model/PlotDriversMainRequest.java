package com.sage.cabapp.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-12-2019.
 * Mindcrew
 * maroofahmedsiddique@gmail.com
 */
public class PlotDriversMainRequest {

    @SerializedName("requestData")
    @Expose
    private PlotDriversRequestData requestData;

    public PlotDriversRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(PlotDriversRequestData requestData) {
        this.requestData = requestData;
    }

}
