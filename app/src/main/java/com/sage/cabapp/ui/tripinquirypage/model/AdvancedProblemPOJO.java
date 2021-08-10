package com.sage.cabapp.ui.tripinquirypage.model;

import com.sage.cabapp.R;

import java.util.Arrays;
import java.util.List;

public class AdvancedProblemPOJO {

    private String title;
    private int icon;

    public AdvancedProblemPOJO(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static List<AdvancedProblemPOJO> getExampleDataset() {
        return Arrays.asList(
                new AdvancedProblemPOJO("Pick the problem type", R.mipmap.ic_launcher_round),
                new AdvancedProblemPOJO("I left my item in the car", R.mipmap.ic_launcher_round),
                new AdvancedProblemPOJO("I have a question about trip’s fee/charge", R.mipmap.ic_launcher_round),
                new AdvancedProblemPOJO("Report and accident", R.mipmap.ic_launcher_round),
                new AdvancedProblemPOJO("Driver issues", R.mipmap.ic_launcher_round),
                new AdvancedProblemPOJO("Other issues", R.mipmap.ic_launcher_round)
        );
    }
}
