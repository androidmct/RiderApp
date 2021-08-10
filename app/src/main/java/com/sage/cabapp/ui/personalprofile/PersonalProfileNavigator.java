package com.sage.cabapp.ui.personalprofile;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;
import com.sage.cabapp.ui.personalprofile.model.ChangeCardPreferencesResponse;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface PersonalProfileNavigator {

    void onBack();

    void addPaymentMethod();

    void successAPI(ChangeCardPreferencesResponse changeCardPreferencesResponse);

    void errorAPI(ANError anError);

    void getAllCardssuccessAPI(GetAllPaymentsCardResponse response);
}
