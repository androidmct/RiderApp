package com.sage.cabapp.ui.setaddress;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;

/**
 * Created by Maroof Ahmed Siddique on 25-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface SetAddressNavigator {

    void onBack();

    void addMoreStopClick();

    void cancelStopClick();

    void clickHome();

    void clickWork();

    void mapClick();

    void confirmSetAddress();

    void successAPI(GetAllPlacesResponse getAllPlacesResponse);

    void errorAPI(ANError anError);
}
