package com.sage.cabapp.ui.tipthemmore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TipThemResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
