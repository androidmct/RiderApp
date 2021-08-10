package com.sage.cabapp.ui.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetAllPaymentsCardMainRequest {

    @SerializedName("requestData")
    @Expose
    private GetAllPaymentsCardRequestData requestData;

    public GetAllPaymentsCardRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(GetAllPaymentsCardRequestData requestData) {
        this.requestData = requestData;
    }
}
