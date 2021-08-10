package com.sage.cabapp.ui.savedplaces.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddPlaceMainRequest {

    @SerializedName("requestData")
    @Expose
    private AddPlaceRequestData requestData;

    public AddPlaceRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(AddPlaceRequestData requestData) {
        this.requestData = requestData;
    }

}
