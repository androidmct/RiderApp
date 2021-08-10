package com.sage.cabapp.ui.addpaymentmethod.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 14-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class ChangeDefaultMethodRequest {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("defaultmethod")
    @Expose
    private String defaultmethod;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDefaultmethod() {
        return defaultmethod;
    }

    public void setDefaultmethod(String defaultmethod) {
        this.defaultmethod = defaultmethod;
    }



}
