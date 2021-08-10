package com.sage.cabapp.ui.addpaymentmethod;

import android.content.Context;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.howtopay.model.HowToPayDatum;
import com.sage.cabapp.ui.howtopay.model.HowToPayMainRequest;
import com.sage.cabapp.ui.howtopay.model.HowToPayMainResponse;
import com.sage.cabapp.ui.howtopay.model.HowToPayRequestData;
import com.sage.cabapp.ui.howtopay.model.PaypalTokenMainRequest;
import com.sage.cabapp.ui.howtopay.model.PaypalTokenMainResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddPaymentMethodViewModel extends BaseViewModel<AddPaymentMethodNavigator> {

    public AddPaymentMethodViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack() {
        getNavigator().backButton();
    }

    public void clickCreditCard() {
        getNavigator().clickCreditCard();
    }

    public void clickPaypal() {
        getNavigator().clickPaypal();
    }
    public void clickCardCamera() {
        getNavigator().clickCardCamera();
    }

    public void clickGooglePay() {
        getNavigator().clickGooglePay();
    }

    public void submitCreditCard() {
        getNavigator().submitCreditCard();
    }

    boolean isEnterCardNumber(String number) {
        return !TextUtils.isEmpty(number);
    }

    boolean isEnterCardName(String name) {
        return !TextUtils.isEmpty(name);
    }

    boolean isEnterCardDate(String date) {
        return !TextUtils.isEmpty(date);
    }

    boolean isEnterCardCVV(String cvv) {
        return !TextUtils.isEmpty(cvv);
    }

    boolean isEnterPaypalEmail(String email) {
        return !TextUtils.isEmpty(email);
    }
    boolean isPaypalEmailValid(String email) {
        return Constant.isEmailValid(email);
    }
    boolean isEnterPaypalPassword(String password) {
        return !TextUtils.isEmpty(password);
    }
    public void submitPaypal() {
        getNavigator().submitPaypal();
    }

    public void submitGooglePay() {
        getNavigator().submitGooglePay();
    }


    void howToPayCreditCardWS(String number, String name, String month,String year, String cvv,String cardType, Context context) {

        String userID = SharedData.getString(context, Constant.USERID);

        HowToPayMainRequest howToPayMainRequest = new HowToPayMainRequest();
        HowToPayRequestData howToPayRequestData = new HowToPayRequestData();
        HowToPayDatum howToPayDatum = new HowToPayDatum();

        howToPayDatum.setCardNumber(number);
        howToPayDatum.setCardName(name);
        howToPayDatum.setExpMonth(month);
        howToPayDatum.setExpYear(year);
        howToPayDatum.setCardCvv(cvv);
        howToPayDatum.setCardType(cardType);
        howToPayDatum.setPaymentType("card");
        howToPayDatum.setUserid(userID);
        howToPayDatum.setDevicetype("Android");

        howToPayRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        howToPayRequestData.setRequestType("RiderCardRegistration");
        howToPayRequestData.setData(howToPayDatum);

        howToPayMainRequest.setRequestData(howToPayRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(howToPayMainRequest)
                .setTag("RiderCardRegistration")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(HowToPayMainResponse.class, new ParsedRequestListener<HowToPayMainResponse>() {

                    @Override
                    public void onResponse(HowToPayMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void howToPayPaypalWS(String email, String password, Context context) {

        String userID = SharedData.getString(context, Constant.USERID);

        HowToPayMainRequest howToPayMainRequest = new HowToPayMainRequest();
        HowToPayRequestData howToPayRequestData = new HowToPayRequestData();
        HowToPayDatum howToPayDatum = new HowToPayDatum();

        howToPayDatum.setPayPalEmail(email);
        howToPayDatum.setPayPalPassword(password);
        howToPayDatum.setPaymentType("paypal");
        howToPayDatum.setUserid(userID);
        howToPayDatum.setDevicetype("Android");

        howToPayRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        howToPayRequestData.setRequestType("RegisterPaymentMethod");
        howToPayRequestData.setData(howToPayDatum);

        howToPayMainRequest.setRequestData(howToPayRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(howToPayMainRequest)
                .setTag("RegisterPaymentMethod")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(HowToPayMainResponse.class, new ParsedRequestListener<HowToPayMainResponse>() {

                    @Override
                    public void onResponse(HowToPayMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void howToGooglePayWS(Context context) {

        String userID = SharedData.getString(context, Constant.USERID);

        HowToPayMainRequest howToPayMainRequest = new HowToPayMainRequest();
        HowToPayRequestData howToPayRequestData = new HowToPayRequestData();
        HowToPayDatum howToPayDatum = new HowToPayDatum();

        howToPayDatum.setPaymentType("gpay");
        howToPayDatum.setUserid(userID);

        howToPayRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        howToPayRequestData.setRequestType("RegisterPaymentMethod");
        howToPayRequestData.setData(howToPayDatum);
        howToPayDatum.setDevicetype("Android");
        howToPayMainRequest.setRequestData(howToPayRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(howToPayMainRequest)
                .setTag("RegisterPaymentMethod")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(HowToPayMainResponse.class, new ParsedRequestListener<HowToPayMainResponse>() {

                    @Override
                    public void onResponse(HowToPayMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void sendPaypalTokenWS(Context context, String paypalToken) {

        String userid = SharedData.getString(context, Constant.USERID);

        PaypalTokenMainRequest paypalTokenMainRequest = new PaypalTokenMainRequest();
        paypalTokenMainRequest.setUserid(userid);
        paypalTokenMainRequest.setPaypaltoken(paypalToken);
        paypalTokenMainRequest.setDevicetype("Android");
        paypalTokenMainRequest.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");

        AndroidNetworking.post(ApiConstants.paypalSendToken)
                .addApplicationJsonBody(paypalTokenMainRequest)
                .setTag("paypalSendToken")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(PaypalTokenMainResponse.class, new ParsedRequestListener<PaypalTokenMainResponse>() {

                    @Override
                    public void onResponse(PaypalTokenMainResponse response) {
                        getNavigator().successPaypalTokenAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }
}
