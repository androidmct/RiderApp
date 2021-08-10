package com.sage.cabapp.ui.editname;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface EditNameNavigator {

    void onBack();

    void saveName();

    void successAPI(ProfileUpdateResponse profileUpdateResponse);

    void errorAPI(ANError anError);
}
