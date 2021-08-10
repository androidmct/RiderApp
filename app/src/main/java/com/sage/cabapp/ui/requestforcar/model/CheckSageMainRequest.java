package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CheckSageMainRequest {

    @SerializedName("requestData")
    @Expose
    private CheckSageRequestData requestData;

    public CheckSageRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(CheckSageRequestData requestData) {
        this.requestData = requestData;
    }
}
