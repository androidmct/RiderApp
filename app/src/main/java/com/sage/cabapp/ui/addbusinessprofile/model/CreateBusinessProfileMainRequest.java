package com.sage.cabapp.ui.addbusinessprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 18-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class CreateBusinessProfileMainRequest {


    @SerializedName("requestData")
    @Expose
    private CreateBusinessProfileRequestData requestData;

    public CreateBusinessProfileRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(CreateBusinessProfileRequestData requestData) {
        this.requestData = requestData;
    }

}
