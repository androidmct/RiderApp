package com.sage.cabapp.ui.payment.model;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class PaymentCardModel {
    public PaymentCardModel(String cardNumber, int cardImg) {
        this.cardNumber = cardNumber;
        this.cardImg = cardImg;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardImg() {
        return cardImg;
    }

    public void setCardImg(int cardImg) {
        this.cardImg = cardImg;
    }

    String cardNumber;
    int cardImg;
}
