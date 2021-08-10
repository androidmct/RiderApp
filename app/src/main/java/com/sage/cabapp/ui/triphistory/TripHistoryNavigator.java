package com.sage.cabapp.ui.triphistory;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.triphistory.model.TripHistoryMainResponse;

/**
 * Created by Maroof Ahmed Siddique on 07-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface TripHistoryNavigator {

    void back();

    void successAPI(TripHistoryMainResponse response);

    void errorAPI(ANError anError);
}
