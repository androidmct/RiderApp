package com.sage.cabapp.ui.editcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class EditUpdateCardMainRequest {

    @SerializedName("requestData")
    @Expose
    private EditUpdateCardRequestData requestData;

    public EditUpdateCardRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(EditUpdateCardRequestData requestData) {
        this.requestData = requestData;
    }
}
