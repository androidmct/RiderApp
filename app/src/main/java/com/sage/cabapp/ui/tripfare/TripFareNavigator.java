package com.sage.cabapp.ui.tripfare;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIResponse;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface TripFareNavigator {

    void onCancel();

    void successAPI(ConstantAPIResponse response);

    void errorAPI(ANError anError);
}
