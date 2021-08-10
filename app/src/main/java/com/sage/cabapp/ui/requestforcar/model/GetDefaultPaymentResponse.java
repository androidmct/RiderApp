package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 18-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GetDefaultPaymentResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("businessId")
    @Expose
    private String businessId;
    @SerializedName("businessBrand")
    @Expose
    private String businessBrand;
    @SerializedName("personalId")
    @Expose
    private String personalId;
    @SerializedName("personalBrand")
    @Expose
    private String personalBrand;

    public String getDefaultmethod() {
        return defaultmethod;
    }

    public void setDefaultmethod(String defaultmethod) {
        this.defaultmethod = defaultmethod;
    }

    @SerializedName("defaultmethod")
    @Expose
    private String defaultmethod;

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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessBrand() {
        return businessBrand;
    }

    public void setBusinessBrand(String businessBrand) {
        this.businessBrand = businessBrand;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalBrand() {
        return personalBrand;
    }

    public void setPersonalBrand(String personalBrand) {
        this.personalBrand = personalBrand;
    }
}
