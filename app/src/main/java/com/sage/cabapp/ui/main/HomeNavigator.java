package com.sage.cabapp.ui.main;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenResponse;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;

/**
 * Created by Maroof Ahmed Siddique on 20-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface HomeNavigator {

    void backButton();

    void openMenu();

    void clickNavigation();

    void chooseDestination();

    void successAPI(UpdateDeviceTokenResponse updateDeviceTokenResponse);

    void errorAPI(ANError anError);

    void navClickTripHistory();
    void navClickPayments();
    void navClickSavedPlaces();
    void navClickPromo();
    void navClickFreeRides();
    void navClickHelp();

    void successSavedPlacesAPI(GetAllPlacesResponse response);
}
