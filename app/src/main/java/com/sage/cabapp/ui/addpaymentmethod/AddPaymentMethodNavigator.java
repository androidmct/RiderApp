package com.sage.cabapp.ui.addpaymentmethod;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.howtopay.model.HowToPayMainResponse;
import com.sage.cabapp.ui.howtopay.model.PaypalTokenMainResponse;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface AddPaymentMethodNavigator {

    void backButton();

    void clickCreditCard();

    void clickPaypal();

    void clickGooglePay();

    void submitCreditCard();

    void submitPaypal();

    void submitGooglePay();
    void clickCardCamera();



    void successAPI(HowToPayMainResponse response);

    void errorAPI(ANError anError);

    void successPaypalTokenAPI(PaypalTokenMainResponse response);
}
