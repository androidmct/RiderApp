package com.sage.cabapp.ui.addnamesignup;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.addnamesignup.model.RegisterDatum;
import com.sage.cabapp.ui.addnamesignup.model.RegisterMainRequest;
import com.sage.cabapp.ui.addnamesignup.model.RegisterMainResponse;
import com.sage.cabapp.ui.addnamesignup.model.RegisterRequestData;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class AddNameSignupViewModel extends BaseViewModel<AddNameSignUpNavigator> {

    public AddNameSignupViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack() {
        getNavigator().backButton();
    }

    public void nextName() {
        getNavigator().nextButton();
    }

    boolean isFirstNameValid(String name) {
        return !TextUtils.isEmpty(name);
    }

    boolean isLastNameValid(String name) {
        return !TextUtils.isEmpty(name);
    }

    void registerUserWS(String number, String email, String firstName, String lastName, String devicetoken) {

        RegisterMainRequest registerMainRequest = new RegisterMainRequest();
        RegisterRequestData registerRequestData = new RegisterRequestData();
        RegisterDatum registerDatum = new RegisterDatum();

        registerDatum.setUserMobile(number);
        registerDatum.setFname(firstName);
        registerDatum.setLname(lastName);
        registerDatum.setUserEmail(email);
        registerDatum.setLat("22.7196");
        registerDatum.setLng("75.8577");
        registerDatum.setDevicetoken(devicetoken);
        registerDatum.setDevicetype("Android");

        registerRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        registerRequestData.setRequestType("NewUserRegister");
        registerRequestData.setData(registerDatum);

        registerMainRequest.setRequestData(registerRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(registerMainRequest)
                .setTag("NewUserRegister")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(RegisterMainResponse.class, new ParsedRequestListener<RegisterMainResponse>() {

                    @Override
                    public void onResponse(RegisterMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
