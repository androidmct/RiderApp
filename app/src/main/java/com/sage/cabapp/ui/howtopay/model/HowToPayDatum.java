package com.sage.cabapp.ui.howtopay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maroof Ahmed Siddique on 27-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class HowToPayDatum {

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("cardName")
    @Expose
    private String cardName;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    @SerializedName("expMonth")
    @Expose
    private String expMonth;
    @SerializedName("expYear")
    @Expose
    private String expYear;
    @SerializedName("cardCvv")
    @Expose
    private String cardCvv;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @SerializedName("cardType")
    @Expose
    private String cardType;

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    @SerializedName("devicetype")
    @Expose
    private String devicetype;


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    @SerializedName("payPalEmail")
    @Expose
    private String payPalEmail;

    public String getPayPalEmail() {
        return payPalEmail;
    }

    public void setPayPalEmail(String payPalEmail) {
        this.payPalEmail = payPalEmail;
    }

    public String getPayPalPassword() {
        return payPalPassword;
    }

    public void setPayPalPassword(String payPalPassword) {
        this.payPalPassword = payPalPassword;
    }

    @SerializedName("payPalPassword")
    @Expose
    private String payPalPassword;
}
