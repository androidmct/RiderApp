package com.sage.cabapp.ui.paymentprofile;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;

/**
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface ChangePaymentProfileNavigator {

    void onBack();
    void personalProfile();
    void businessProfile();
    void editbusinessProfile();

    void successAPI(GetAllPaymentsCardResponse response);

    void errorAPI(ANError anError);
}
