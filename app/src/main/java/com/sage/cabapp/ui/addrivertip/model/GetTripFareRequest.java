package com.sage.cabapp.ui.addrivertip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 04-06-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class GetTripFareRequest {

    @SerializedName("requestId")
    @Expose
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}
