package com.sage.cabapp.ui.verifyotp;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneMainResponse;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface VerifyOTPNavigator {

    void backButton();

    void nextButton();

    void resendCode();

    void successAPI(CheckPhoneMainResponse checkPhoneMainResponse);

    void errorAPI(ANError anError);
}
