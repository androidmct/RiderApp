package com.sage.cabapp.ui.addnamesignup;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.addnamesignup.model.RegisterMainResponse;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface AddNameSignUpNavigator {

    void backButton();

    void nextButton();

    void successAPI(RegisterMainResponse response);

    void errorAPI(ANError anError);
}
