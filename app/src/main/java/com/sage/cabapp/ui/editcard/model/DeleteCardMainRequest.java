package com.sage.cabapp.ui.editcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DeleteCardMainRequest {

    @SerializedName("requestData")
    @Expose
    private DeleteCardRequestData requestData;

    public DeleteCardRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(DeleteCardRequestData requestData) {
        this.requestData = requestData;
    }
}
