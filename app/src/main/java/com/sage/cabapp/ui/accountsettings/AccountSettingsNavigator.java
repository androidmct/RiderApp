package com.sage.cabapp.ui.accountsettings;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
import com.sage.cabapp.ui.accountsettings.model.UpdatedProfileResponse;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface AccountSettingsNavigator {

    void onBack();

    void logout();

    void changeprofilepic();

    void editName();

    void editEmail();

    void editPhone();

    void successAPI(UpdatedProfileResponse profileUpdateResponse);

    void deleteSuccessAPI(ProfileUpdateResponse profileUpdateResponse);

    void errorAPI(ANError anError);
}
