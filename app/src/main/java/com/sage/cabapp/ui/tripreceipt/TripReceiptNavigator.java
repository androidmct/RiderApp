package com.sage.cabapp.ui.tripreceipt;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoyDetailsResponse;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface TripReceiptNavigator {

    void onBack();

    void tipThemMore();

    void successAPI(TripHistoyDetailsResponse response);

    void errorAPI(ANError anError);

}
