package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 06-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class RequestSageMainRequest {

    @SerializedName("requestData")
    @Expose
    private RequestSageRequestData data;

    public RequestSageRequestData getData() {
        return data;
    }

    public void setData(RequestSageRequestData data) {
        this.data = data;
    }


}
