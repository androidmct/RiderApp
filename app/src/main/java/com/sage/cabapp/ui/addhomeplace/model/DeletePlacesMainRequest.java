package com.sage.cabapp.ui.addhomeplace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 31-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DeletePlacesMainRequest {

    @SerializedName("requestData")
    @Expose
    private DeletePlacesRequestData requestData;

    public DeletePlacesRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(DeletePlacesRequestData requestData) {
        this.requestData = requestData;
    }

}
