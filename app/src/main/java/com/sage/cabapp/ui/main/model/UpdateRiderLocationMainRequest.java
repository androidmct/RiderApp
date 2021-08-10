package com.sage.cabapp.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-12-2019.
 * Mindcrew
 * maroofahmedsiddique@gmail.com
 */
public class UpdateRiderLocationMainRequest {

    @SerializedName("requestData")
    @Expose
    private UpdateRiderLocationRequestData requestData;

    public UpdateRiderLocationRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(UpdateRiderLocationRequestData requestData) {
        this.requestData = requestData;
    }
}
