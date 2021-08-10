package com.sage.cabapp.ui.addbusinessprofile;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileResponse;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodResponse;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface NewBusinessProfileNavigator {

    void onBack();

    void addPaymentMethod();

    void createBusinessProfile();

    void successAPI(GetAllPaymentsCardResponse response);

    void successCreateBusinessAPI(CreateBusinessProfileResponse response);

    void successDefaultMethodAPI(ChangeDefaultMethodResponse response);

    void errorAPI(ANError anError);
}
