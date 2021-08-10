package com.sage.cabapp.ui.updateroute;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedMainResponse;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;

/**
 * Created by Maroof Ahmed Siddique on 25-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface UpdateRouteNavigator {

    void onBack();

    void addMoreStopClick();

    void cancelStopClick();

    void clickHome();

    void clickWork();

    void confirmSetAddress();

    void successAPI(GetAllPlacesResponse getAllPlacesResponse);

    void sageAcceptedResponse(RideAcceptedMainResponse rideAcceptedMainResponse);

    void errorAPI(ANError anError);
}
