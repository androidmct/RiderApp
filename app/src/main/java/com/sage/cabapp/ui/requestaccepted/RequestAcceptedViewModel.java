package com.sage.cabapp.ui.requestaccepted;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.chatmodulenew.model.CheckLastActiveChannelRequest;
import com.sage.cabapp.ui.chatmodulenew.model.CheckLastActiveChannelResponse;
import com.sage.cabapp.ui.requestaccepted.model.CheckAcceptedRideStatusDatum;
import com.sage.cabapp.ui.requestaccepted.model.CheckAcceptedRideStatusMainRequest;
import com.sage.cabapp.ui.requestaccepted.model.CheckAcceptedRideStatusRequestData;
import com.sage.cabapp.ui.requestaccepted.model.CheckAcceptedRideStatusResponse;
import com.sage.cabapp.ui.requestaccepted.model.CheckCancelTimeDatum;
import com.sage.cabapp.ui.requestaccepted.model.CheckCancelTimeMainRequest;
import com.sage.cabapp.ui.requestaccepted.model.CheckCancelTimeMainResponse;
import com.sage.cabapp.ui.requestaccepted.model.CheckCancelTimeRequestData;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedDatum;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedMainRequest;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedMainResponse;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedRequestData;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentDatum;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentMainRequest;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentRequestData;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

public class RequestAcceptedViewModel extends BaseViewModel<RequestAcceptedNavigator> {

    public RequestAcceptedViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void openMenu() {
        getNavigator().openMenu();
    }

    public void cancelRequest() {
        getNavigator().cancelRequest();
    }

    public void clickUpdateAddress() {
        getNavigator().clickUpdateAddress();
    }

    public void phoneCall() {
        getNavigator().phoneCall();
    }

    public void chatMessage() {
        getNavigator().chatMessage();
    }

    public void openDetails() {
        getNavigator().openDetails();
    }

    public void checkDriver() {
        getNavigator().checkDriver();
    }

    public void call911() {
        getNavigator().call911();
    }

    public void clickNavigation() {
        getNavigator().clickNavigation();
    }

    public void navClickTripHistory() {
        getNavigator().navClickTripHistory();
    }

    public void navClickPayments() {
        getNavigator().navClickPayments();
    }

    public void navClickSavedPlaces() {
        getNavigator().navClickSavedPlaces();
    }

    public void navClickPromo() {
        getNavigator().navClickPromo();
    }

    public void navClickFreeRides() {
        getNavigator().navClickFreeRides();
    }

    public void navClickHelp() {
        getNavigator().navClickHelp();
    }

    void acceptedRequestedWS(Context context) {

        String requestID = SharedData.getString(context, Constant.PERM_REQUEST_ID);

        RideAcceptedMainRequest rideAcceptedMainRequest = new RideAcceptedMainRequest();
        RideAcceptedRequestData rideAcceptedRequestData = new RideAcceptedRequestData();
        RideAcceptedDatum rideAcceptedDatum = new RideAcceptedDatum();

        rideAcceptedDatum.setRequestId(requestID);
        rideAcceptedDatum.setDevicetype("Android");

        rideAcceptedRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        rideAcceptedRequestData.setRequestType("riderRequestAvalible");
        rideAcceptedRequestData.setData(rideAcceptedDatum);

        rideAcceptedMainRequest.setRequestData(rideAcceptedRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(rideAcceptedMainRequest)
                .setTag("riderRequestAvalible")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(RideAcceptedMainResponse.class, new ParsedRequestListener<RideAcceptedMainResponse>() {

                    @Override
                    public void onResponse(RideAcceptedMainResponse response) {
                        getNavigator().sageAcceptedResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void cancelRequestTimeWS(Context context) {

        String requestID = SharedData.getString(context, Constant.PERM_REQUEST_ID);
        String userid = SharedData.getString(context, Constant.USERID);

        CheckCancelTimeMainRequest checkCancelTimeMainRequest = new CheckCancelTimeMainRequest();
        CheckCancelTimeRequestData checkCancelTimeRequestData = new CheckCancelTimeRequestData();
        CheckCancelTimeDatum checkCancelTimeDatum = new CheckCancelTimeDatum();

        checkCancelTimeDatum.setRequestId(requestID);
        checkCancelTimeDatum.setUserid(userid);
        checkCancelTimeDatum.setDevicetype("Android");

        checkCancelTimeRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        checkCancelTimeRequestData.setRequestType("riderRequestTime");
        checkCancelTimeRequestData.setData(checkCancelTimeDatum);

        checkCancelTimeMainRequest.setRequestData(checkCancelTimeRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(checkCancelTimeMainRequest)
                .setTag("riderRequestTime")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CheckCancelTimeMainResponse.class, new ParsedRequestListener<CheckCancelTimeMainResponse>() {

                    @Override
                    public void onResponse(CheckCancelTimeMainResponse response) {
                        getNavigator().checkCancelTime(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void checkRideStatusWS(Context context, boolean value) {

        String requestID = SharedData.getString(context, Constant.PERM_REQUEST_ID);
        String userid = SharedData.getString(context, Constant.USERID);
        String duserid = SharedData.getString(context, Constant.DRIVER_USERID);

        CheckAcceptedRideStatusMainRequest checkAcceptedRideStatusMainRequest = new CheckAcceptedRideStatusMainRequest();
        CheckAcceptedRideStatusRequestData checkAcceptedRideStatusRequestData = new CheckAcceptedRideStatusRequestData();
        CheckAcceptedRideStatusDatum checkAcceptedRideStatusDatum = new CheckAcceptedRideStatusDatum();

        checkAcceptedRideStatusDatum.setUserid(userid);
        checkAcceptedRideStatusDatum.setDuserid(duserid);
        checkAcceptedRideStatusDatum.setRequestId(requestID);
        checkAcceptedRideStatusDatum.setDevicetype("Android");

        checkAcceptedRideStatusRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        checkAcceptedRideStatusRequestData.setRequestType("checkDriverRiderStatus");
        checkAcceptedRideStatusRequestData.setData(checkAcceptedRideStatusDatum);

        checkAcceptedRideStatusMainRequest.setRequestData(checkAcceptedRideStatusRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(checkAcceptedRideStatusMainRequest)
                .setTag("checkDriverRiderStatus")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CheckAcceptedRideStatusResponse.class, new ParsedRequestListener<CheckAcceptedRideStatusResponse>() {

                    @Override
                    public void onResponse(CheckAcceptedRideStatusResponse response) {
                        getNavigator().sageSageStatusResponse(response, value);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPIStatus(anError);
                    }
                });
    }

    void getDefaultPaymentWS(Context context) {

        String userid = SharedData.getString(context,Constant.USERID);

        GetDefaultPaymentMainRequest getDefaultPaymentMainRequest = new GetDefaultPaymentMainRequest();
        GetDefaultPaymentRequestData getDefaultPaymentRequestData = new GetDefaultPaymentRequestData();
        GetDefaultPaymentDatum getDefaultPaymentDatum = new GetDefaultPaymentDatum();

        getDefaultPaymentDatum.setUserid(userid);
        getDefaultPaymentDatum.setDevicetype("Android");

        getDefaultPaymentRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        getDefaultPaymentRequestData.setRequestType("GetBusinessAndPersonal");
        getDefaultPaymentRequestData.setData(getDefaultPaymentDatum);

        getDefaultPaymentMainRequest.setRequestData(getDefaultPaymentRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(getDefaultPaymentMainRequest)
                .setTag("GetBusinessAndPersonal")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GetDefaultPaymentResponse.class, new ParsedRequestListener<GetDefaultPaymentResponse>() {

                    @Override
                    public void onResponse(GetDefaultPaymentResponse response) {
                        getNavigator().successPaymentsAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }

    void riderTwilioWS(Context context) {

        String riderID = SharedData.getString(context, Constant.USERID);

        CheckLastActiveChannelRequest checkLastActiveChannelRequest = new CheckLastActiveChannelRequest();
        checkLastActiveChannelRequest.setRiderId(riderID);

        AndroidNetworking.post(ApiConstants.riderTwilio)
                .addApplicationJsonBody(checkLastActiveChannelRequest)
                .setTag("riderTwilio")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CheckLastActiveChannelResponse.class, new ParsedRequestListener<CheckLastActiveChannelResponse>() {

                    @Override
                    public void onResponse(CheckLastActiveChannelResponse response) {
                        getNavigator().checkLastTwilioChatResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }



}
