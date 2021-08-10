package com.sage.cabapp.ui.verifyotp;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneDatum;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneMainRequest;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneMainResponse;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneRequestData;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class VerifyOTPViewModel extends BaseViewModel<VerifyOTPNavigator> {

    public VerifyOTPViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack() {
        getNavigator().backButton();
    }

    public void checkCode() {
        getNavigator().nextButton();
    }

    boolean isOTPValid(String otp) {
        return !TextUtils.isEmpty(otp);
    }

    public void resendCode() {
        getNavigator().resendCode();
    }

    void checkPhoneWS(String number) {

        CheckPhoneMainRequest checkPhoneMainRequest = new CheckPhoneMainRequest();
        CheckPhoneRequestData checkPhoneRequestData = new CheckPhoneRequestData();
        CheckPhoneDatum checkPhoneDatum = new CheckPhoneDatum();

        checkPhoneDatum.setUserMobile(number);
        checkPhoneDatum.setDevicetype("Android");

        checkPhoneRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        checkPhoneRequestData.setRequestType("userSignupCheck");
        checkPhoneRequestData.setData(checkPhoneDatum);

        checkPhoneMainRequest.setRequestData(checkPhoneRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(checkPhoneMainRequest)
                .setTag("userSignupCheck")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CheckPhoneMainResponse.class, new ParsedRequestListener<CheckPhoneMainResponse>() {

                    @Override
                    public void onResponse(CheckPhoneMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
