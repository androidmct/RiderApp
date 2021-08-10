package com.sage.cabapp.ui.howtopay;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodResponse;
import com.sage.cabapp.ui.howtopay.model.HowToPayMainResponse;
import com.sage.cabapp.ui.howtopay.model.PaypalTokenMainResponse;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface HowToPayNavigator {

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

    void successDefaultMethodAPI(ChangeDefaultMethodResponse response);
    void successPaypalTokenAPI(PaypalTokenMainResponse response);
}
