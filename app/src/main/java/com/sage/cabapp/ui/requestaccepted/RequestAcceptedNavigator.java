package com.sage.cabapp.ui.requestaccepted;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.chatmodulenew.model.CheckLastActiveChannelResponse;
import com.sage.cabapp.ui.requestaccepted.model.CheckAcceptedRideStatusResponse;
import com.sage.cabapp.ui.requestaccepted.model.CheckCancelTimeMainResponse;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedMainResponse;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;

public interface RequestAcceptedNavigator {

    void openMenu();

    void cancelRequest();

    void call911();
    void clickNavigation();

    void phoneCall();
    void clickUpdateAddress();

    void chatMessage();

    void openDetails();
    void checkDriver();

    void sageAcceptedResponse(RideAcceptedMainResponse rideAcceptedMainResponse);

    void checkCancelTime(CheckCancelTimeMainResponse checkCancelTimeMainResponse);

    void errorAPI(ANError anError);

    void errorAPIStatus(ANError anError);

    void sageSageStatusResponse(CheckAcceptedRideStatusResponse checkAcceptedRideStatusResponse, boolean value);

    void navClickTripHistory();
    void navClickPayments();
    void navClickSavedPlaces();
    void navClickPromo();
    void navClickFreeRides();
    void navClickHelp();

    void successPaymentsAPI(GetDefaultPaymentResponse response);

    void checkLastTwilioChatResponse(CheckLastActiveChannelResponse response);

}
