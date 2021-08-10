package com.sage.cabapp.ui.chatmodulenew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-04-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class CheckLastActiveChannelRequest {

    @SerializedName("rider_id")
    @Expose
    private String riderId;

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }
}
