package com.sage.cabapp.ui.savedplaces.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetAllPlacesMainRequest {

    @SerializedName("requestData")
    @Expose
    private GetAllPlacesRequestData requestData;

    public GetAllPlacesRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(GetAllPlacesRequestData requestData) {
        this.requestData = requestData;
    }
}
