package com.sage.cabapp.ui.personalprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ChangeCardPreferencesMainRequest {

    @SerializedName("requestData")
    @Expose
    private ChangeCardPreferencesRequestData requestData;

    public ChangeCardPreferencesRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(ChangeCardPreferencesRequestData requestData) {
        this.requestData = requestData;
    }

}
