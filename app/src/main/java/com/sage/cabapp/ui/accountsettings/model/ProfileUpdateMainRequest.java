package com.sage.cabapp.ui.accountsettings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 20-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ProfileUpdateMainRequest {

    @SerializedName("requestData")
    @Expose
    private ProfileUpdateRequestData requestData;

    public ProfileUpdateRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(ProfileUpdateRequestData requestData) {
        this.requestData = requestData;
    }

}
