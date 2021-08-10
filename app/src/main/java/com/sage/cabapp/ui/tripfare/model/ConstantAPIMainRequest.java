package com.sage.cabapp.ui.tripfare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ConstantAPIMainRequest {

    @SerializedName("requestData")
    @Expose
    private ConstantAPIRequestData requestData;

    public ConstantAPIRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(ConstantAPIRequestData requestData) {
        this.requestData = requestData;
    }
}
