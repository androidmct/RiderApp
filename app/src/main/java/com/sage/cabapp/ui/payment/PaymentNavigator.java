package com.sage.cabapp.ui.payment;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface PaymentNavigator {

    void onBack();

    void addPaymentMethod();

    void personalProfile();

    void businessProfile();
    void editbusinessProfile();

    void successAPI(GetAllPaymentsCardResponse response);

    void errorAPI(ANError anError);
}
