package com.sage.cabapp.ui.updateroute.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 23-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class UpdateRouteMainRequest {


    @SerializedName("requestData")
    @Expose
    private UpdateRouteRequestData requestData;

    public UpdateRouteRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(UpdateRouteRequestData requestData) {
        this.requestData = requestData;
    }
}
