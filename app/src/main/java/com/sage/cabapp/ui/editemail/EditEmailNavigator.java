package com.sage.cabapp.ui.editemail;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface EditEmailNavigator {

    void onBack();

    void nextEmail();

    void successAPI(ProfileUpdateResponse profileUpdateResponse);

    void errorAPI(ANError anError);
}
