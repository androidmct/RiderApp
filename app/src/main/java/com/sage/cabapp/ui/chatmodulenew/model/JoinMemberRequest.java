package com.sage.cabapp.ui.chatmodulenew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 25-04-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class JoinMemberRequest {

    @SerializedName("rider_id")
    @Expose
    private String riderId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("rider_name")
    @Expose
    private String riderName;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("channel_name")
    @Expose
    private String channelName;
    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("joined_member_id")
    @Expose
    private String joinedMemberId;

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getJoinedMemberId() {
        return joinedMemberId;
    }

    public void setJoinedMemberId(String joinedMemberId) {
        this.joinedMemberId = joinedMemberId;
    }

}
