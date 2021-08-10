package com.sage.cabapp.ui.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Maroof Ahmed Siddique on 14-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AllCardsThreeDSecureUsage implements Serializable {

    @SerializedName("supported")
    @Expose
    private Boolean supported;

    public Boolean getSupported() {
        return supported;
    }

    public void setSupported(Boolean supported) {
        this.supported = supported;
    }
}
