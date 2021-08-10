package com.sage.cabapp.ui.addworkplace;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.savedplaces.model.AddPlaceResponse;

/**
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface AddWorkPlaceNavigator {

    void onBack();

    void submitHomeAddress();

    void fromMap();
    void delete();

    void successAPI(AddPlaceResponse addPlaceResponse);

    void errorAPI(ANError anError);
}
