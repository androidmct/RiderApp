package com.sage.cabapp.ui.howtopay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class HowToPayMainRequest {

    @SerializedName("requestData")
    @Expose
    private HowToPayRequestData requestData;

    public HowToPayRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(HowToPayRequestData requestData) {
        this.requestData = requestData;
    }
}
