package com.sage.cabapp.ui.chatmodulenew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Maroof Ahmed Siddique on 25-04-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class JoinMemberResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<JoinMemberData> data = null;

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

    public List<JoinMemberData> getData() {
        return data;
    }

    public void setData(List<JoinMemberData> data) {
        this.data = data;
    }

}
