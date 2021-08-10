package com.sage.cabapp.ui.editbusinessprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 18-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DeleteBusinessProfileMainRequest {

    @SerializedName("requestData")
    @Expose
    private DeleteBusinessProfileRequestData requestData;

    public DeleteBusinessProfileRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(DeleteBusinessProfileRequestData requestData) {
        this.requestData = requestData;
    }
}
