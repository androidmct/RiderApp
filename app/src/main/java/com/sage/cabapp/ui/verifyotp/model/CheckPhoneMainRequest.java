package com.sage.cabapp.ui.verifyotp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class CheckPhoneMainRequest {

    @SerializedName("requestData")
    @Expose
    private CheckPhoneRequestData requestData;

    public CheckPhoneRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(CheckPhoneRequestData requestData) {
        this.requestData = requestData;
    }
}
