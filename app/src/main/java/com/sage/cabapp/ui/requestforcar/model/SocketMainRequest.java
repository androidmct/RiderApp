package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 15-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class SocketMainRequest {

    @SerializedName("requestData")
    @Expose
    private SocketRequestData requestData;

    public SocketRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(SocketRequestData requestData) {
        this.requestData = requestData;
    }

}
