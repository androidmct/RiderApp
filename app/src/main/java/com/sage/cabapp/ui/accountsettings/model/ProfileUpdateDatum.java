package com.sage.cabapp.ui.accountsettings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 20-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ProfileUpdateDatum {


    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;

    public String getUseremail() {
        return userEmail;
    }

    public void setUseremail(String useremail) {
        this.userEmail = useremail;
    }

    @SerializedName("userEmail")
    @Expose
    private String userEmail;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    @SerializedName("userMobile")
    @Expose
    private String userMobile;

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }

    @SerializedName("UserProfile")
    @Expose
    private String UserProfile;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
