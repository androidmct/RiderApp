package com.sage.cabapp.ui.tripinquirypage;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.tripinquirypage.model.TripInquiryResponse;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface TripInquiryNavigator {

    void onBack();
    void submitInquiry();

    void addImagesClick();

    void successAPI(TripInquiryResponse tripInquiryResponse);

    void errorAPI(ANError anError);
}
