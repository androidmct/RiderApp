package com.sage.cabapp.ui.editphone;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;

/**
 * Created by Maroof Ahmed Siddique on 10-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface UpdatePhoneNavigator {

    void onBack();
    void updateNumber();
    void resendCode();

    void successAPI(ProfileUpdateResponse profileUpdateResponse);

    void errorAPI(ANError anError);
}
