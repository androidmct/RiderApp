package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 18-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetDefaultPaymentMainRequest {

    @SerializedName("requestData")
    @Expose
    private GetDefaultPaymentRequestData requestData;

    public GetDefaultPaymentRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(GetDefaultPaymentRequestData requestData) {
        this.requestData = requestData;
    }

}
