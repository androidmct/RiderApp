package com.sage.cabapp.ui.tripreceipt.model;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class HelpNeededModel {
    public HelpNeededModel(String helpText, String helpID) {
        this.helpText = helpText;
        this.helpID = helpID;
    }

    String helpText = "";

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getHelpID() {
        return helpID;
    }

    public void setHelpID(String helpID) {
        this.helpID = helpID;
    }

    String helpID = "";
}
