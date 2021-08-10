package com.sage.cabapp.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 10-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class UpdateDeviceTokenMainRequest {

    @SerializedName("requestData")
    @Expose
    private UpdateDeviceTokenRequestData requestData;

    public UpdateDeviceTokenRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(UpdateDeviceTokenRequestData requestData) {
        this.requestData = requestData;
    }

}
