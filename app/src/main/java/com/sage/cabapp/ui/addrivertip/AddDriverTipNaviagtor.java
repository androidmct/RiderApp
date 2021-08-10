package com.sage.cabapp.ui.addrivertip;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.addrivertip.model.GetTripFareResponse;
import com.sage.cabapp.ui.addrivertip.model.RatingDriverResponse;

public interface AddDriverTipNaviagtor {

    void onBack();
    void submitTip();

    void tipDriverResponse(RatingDriverResponse ratingDriverResponse);

    void errorAPI(ANError anError);

    void getRideFareResponse(GetTripFareResponse getTripFareResponse);

}
