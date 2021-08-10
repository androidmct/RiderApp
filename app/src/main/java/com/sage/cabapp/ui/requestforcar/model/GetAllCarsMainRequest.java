package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 31-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetAllCarsMainRequest {

    @SerializedName("requestData")
    @Expose
    private GetAllCarsRequestData requestData;

    public GetAllCarsRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(GetAllCarsRequestData requestData) {
        this.requestData = requestData;
    }

}
