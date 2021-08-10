package com.sage.cabapp.ui.tipthemmore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TipThemMainRequest {

    @SerializedName("requestData")
    @Expose
    private TipThemRequestData requestData;

    public TipThemRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(TipThemRequestData requestData) {
        this.requestData = requestData;
    }

}
