package com.sage.cabapp.ui.addbusinessprofile.model;

public class NewBusinessProfileModel {

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    String paymentId;
    String cardNumber;




    public String getCardNumber() {
        return cardNumber;
    }

    public NewBusinessProfileModel(String paymentId,String cardNumber, String cardType, int cardImg, boolean isTicked) {
        this.paymentId = paymentId;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardImg = cardImg;
        this.isTicked = isTicked;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCardImg() {
        return cardImg;
    }

    public void setCardImg(int cardImg) {
        this.cardImg = cardImg;
    }

    public boolean isTicked() {
        return isTicked;
    }

    public void setTicked(boolean ticked) {
        isTicked = ticked;
    }

    String cardType;
    int cardImg;
    boolean isTicked;


}
