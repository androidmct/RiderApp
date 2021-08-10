package com.sage.cabapp.ui.help;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.tripinquirypage.model.TripInquiryResponse;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface GeneralInquiryFormNavigator {

    void onBack();

    void submitQuery();

    void addImagesClick();

    void successAPI(TripInquiryResponse response);

    void errorAPI(ANError anError);
}
