package com.sage.cabapp.ui.driverprofile;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileMainResponse;

/**
 * Created by Maroof Ahmed Siddique on 24-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface DriverProfileNavigator {

    void onBack();

    void successAPI(DriverProfileMainResponse driverProfileMainResponse);

    void errorAPI(ANError anError);
}
