package com.sage.cabapp.ui.requestaccepted.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CheckCancelTimeMainRequest {

    @SerializedName("requestData")
    @Expose
    private CheckCancelTimeRequestData requestData;

    public CheckCancelTimeRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(CheckCancelTimeRequestData requestData) {
        this.requestData = requestData;
    }

}
