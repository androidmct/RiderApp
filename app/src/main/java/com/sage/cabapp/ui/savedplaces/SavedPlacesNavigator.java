package com.sage.cabapp.ui.savedplaces;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface SavedPlacesNavigator {

    void onBack();

    void clickHome();

    void clickWork();
    void clickOtherPlaces();

    void successAPI(GetAllPlacesResponse getAllPlacesResponse);

    void errorAPI(ANError anError);
}
