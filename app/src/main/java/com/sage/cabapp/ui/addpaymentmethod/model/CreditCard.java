package com.sage.cabapp.ui.addpaymentmethod.model;

import java.util.ArrayList;

/**
 * Created by Maroof Ahmed Siddique on 12-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public final class CreditCard {

    ArrayList<Integer> possibleLengths;
    String cardName;
    int drawable;

    public CreditCard(String cardName, ArrayList<Integer> possibleLengths, int drawable) {
        this.cardName = cardName;
        this.possibleLengths = possibleLengths;
        this.drawable = drawable;
    }

    public String getCardName() {
        return this.cardName;
    }

    public int getMaxLength() {
        return possibleLengths.get(possibleLengths.size() - 1);
    }

    public ArrayList<Integer> getPossibleLengths() {
        return this.possibleLengths;
    }

    public int getDrawable() {
        return this.drawable;
    }
}
