package com.sage.cabapp.ui.accountsettings;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateDatum;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateMainRequest;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateRequestData;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
import com.sage.cabapp.ui.accountsettings.model.UpdatedProfileResponse;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AccountSettingsViewModel extends BaseViewModel<AccountSettingsNavigator> {
    public AccountSettingsViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void logout(){
        getNavigator().logout();
    }

    public void changeprofilepic(){
        getNavigator().changeprofilepic();
    }

    public void editName(){
        getNavigator().editName();
    }

    public void editEmail(){
        getNavigator().editEmail();
    }

    public void editPhone(){
        getNavigator().editPhone();
    }

    void getProfileDataWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);

        ProfileUpdateMainRequest profileUpdateMainRequest = new ProfileUpdateMainRequest();
        ProfileUpdateRequestData profileUpdateRequestData = new ProfileUpdateRequestData();
        ProfileUpdateDatum profileUpdateDatum = new ProfileUpdateDatum();

        profileUpdateDatum.setUserid(userid);
        profileUpdateDatum.setDevicetype("Android");

        profileUpdateRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        profileUpdateRequestData.setRequestType("RiderGetData");
        profileUpdateRequestData.setData(profileUpdateDatum);

        profileUpdateMainRequest.setRequestData(profileUpdateRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(profileUpdateMainRequest)
                .setTag("RiderGetData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UpdatedProfileResponse.class, new ParsedRequestListener<UpdatedProfileResponse>() {

                    @Override
                    public void onResponse(UpdatedProfileResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }

    void deletePhotoWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);

        ProfileUpdateMainRequest profileUpdateMainRequest = new ProfileUpdateMainRequest();
        ProfileUpdateRequestData profileUpdateRequestData = new ProfileUpdateRequestData();
        ProfileUpdateDatum profileUpdateDatum = new ProfileUpdateDatum();

        profileUpdateDatum.setUserid(userid);
        profileUpdateDatum.setUserProfile("");
        profileUpdateDatum.setDevicetype("Android");

        profileUpdateRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        profileUpdateRequestData.setRequestType("riderProfileImageRemove");
        profileUpdateRequestData.setData(profileUpdateDatum);

        profileUpdateMainRequest.setRequestData(profileUpdateRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(profileUpdateMainRequest)
                .setTag("riderProfileImageRemove")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ProfileUpdateResponse.class, new ParsedRequestListener<ProfileUpdateResponse>() {

                    @Override
                    public void onResponse(ProfileUpdateResponse response) {
                        getNavigator().deleteSuccessAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }

}
