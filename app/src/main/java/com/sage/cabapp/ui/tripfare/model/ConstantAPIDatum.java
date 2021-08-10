package com.sage.cabapp.ui.tripfare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 30-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ConstantAPIDatum {

    @SerializedName("id")
    @Expose
    private String id;

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    @SerializedName("devicetype")
    @Expose
    private String devicetype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
