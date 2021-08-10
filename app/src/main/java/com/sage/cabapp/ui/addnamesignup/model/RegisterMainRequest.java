package com.sage.cabapp.ui.addnamesignup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class RegisterMainRequest {

    @SerializedName("requestData")
    @Expose
    private RegisterRequestData requestData;

    public RegisterRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RegisterRequestData requestData) {
        this.requestData = requestData;
    }


}
