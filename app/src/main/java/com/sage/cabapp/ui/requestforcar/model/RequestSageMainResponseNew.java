package com.sage.cabapp.ui.requestforcar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Maroof Ahmed Siddique on 15-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class RequestSageMainResponseNew {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<RequestSageResponseDataNew> data = null;
    @SerializedName("requestID")
    @Expose
    private String requestID;
    @SerializedName("request_timeout")
    @Expose
    private Integer requestTimeout;

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

    public List<RequestSageResponseDataNew> getData() {
        return data;
    }

    public void setData(List<RequestSageResponseDataNew> data) {
        this.data = data;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }


}
