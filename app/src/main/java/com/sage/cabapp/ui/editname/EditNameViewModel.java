package com.sage.cabapp.ui.editname;

import android.content.Context;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateDatum;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateMainRequest;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateRequestData;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
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
public class EditNameViewModel extends BaseViewModel<EditNameNavigator> {
    public EditNameViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void saveName(){
        getNavigator().saveName();
    }

    boolean isFirstNameValid(String name) {
        return !TextUtils.isEmpty(name);
    }

    boolean isLastNameValid(String name) {
        return !TextUtils.isEmpty(name);
    }

    void updateNameWS(Context context,String fname, String lname) {

        String userid = SharedData.getString(context, Constant.USERID);

        ProfileUpdateMainRequest profileUpdateMainRequest = new ProfileUpdateMainRequest();
        ProfileUpdateRequestData profileUpdateRequestData = new ProfileUpdateRequestData();
        ProfileUpdateDatum profileUpdateDatum = new ProfileUpdateDatum();

        profileUpdateDatum.setUserid(userid);
        profileUpdateDatum.setDevicetype("Android");
        profileUpdateDatum.setFname(fname);
        profileUpdateDatum.setLname(lname);

        profileUpdateRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        profileUpdateRequestData.setRequestType("profileNameUpdate");
        profileUpdateRequestData.setData(profileUpdateDatum);

        profileUpdateMainRequest.setRequestData(profileUpdateRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(profileUpdateMainRequest)
                .setTag("profileNameUpdate")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ProfileUpdateResponse.class, new ParsedRequestListener<ProfileUpdateResponse>() {

                    @Override
                    public void onResponse(ProfileUpdateResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }
}
