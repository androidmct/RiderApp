package com.sage.cabapp.ui.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Maroof Ahmed Siddique on 14-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AllCards implements Serializable {

    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("checks")
    @Expose
    private AllCardsChecks checks;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("exp_month")
    @Expose
    private Integer expMonth;
    @SerializedName("exp_year")
    @Expose
    private Integer expYear;
    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;
    @SerializedName("funding")
    @Expose
    private String funding;
    @SerializedName("generated_from")
    @Expose
    private String generatedFrom;
    @SerializedName("last4")
    @Expose
    private String last4;
    @SerializedName("three_d_secure_usage")
    @Expose
    private AllCardsThreeDSecureUsage threeDSecureUsage;
    @SerializedName("wallet")
    @Expose
    private Object wallet;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public AllCardsChecks getChecks() {
        return checks;
    }

    public void setChecks(AllCardsChecks checks) {
        this.checks = checks;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getGeneratedFrom() {
        return generatedFrom;
    }

    public void setGeneratedFrom(String generatedFrom) {
        this.generatedFrom = generatedFrom;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public AllCardsThreeDSecureUsage getThreeDSecureUsage() {
        return threeDSecureUsage;
    }

    public void setThreeDSecureUsage(AllCardsThreeDSecureUsage threeDSecureUsage) {
        this.threeDSecureUsage = threeDSecureUsage;
    }

    public Object getWallet() {
        return wallet;
    }

    public void setWallet(Object wallet) {
        this.wallet = wallet;
    }
}
