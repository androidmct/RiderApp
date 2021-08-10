package com.sage.cabapp.ui.requestforcar;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsResponse;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;
import com.sage.cabapp.ui.requestforcar.model.RequestSageMainResponseNew;
import com.sage.cabapp.ui.requestforcar.model.RequestSageResponse;

/**
 * Created by Maroof Ahmed Siddique on 16-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface RequestForCarNavigator {

    void onBack();

    void requestSageClick();

    void clickNavigation();

    void clickTripFare();

    void closeCarDetails();

    void clickPaymentMethod();

    void successAPI(RequestSageResponse requestSageResponse);

    void errorAPI(ANError anError);

    void successAllCarsAPI(GetAllCarsResponse response);

    void successPaymentsAPI(GetDefaultPaymentResponse response);

    void successRequestSageNew(RequestSageMainResponseNew response);
}
