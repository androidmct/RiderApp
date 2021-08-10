package com.sage.cabapp.ui.addhomeplace;

import android.content.Context;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.savedplaces.model.AddPlaceDatum;
import com.sage.cabapp.ui.savedplaces.model.AddPlaceMainRequest;
import com.sage.cabapp.ui.savedplaces.model.AddPlaceRequestData;
import com.sage.cabapp.ui.savedplaces.model.AddPlaceResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddHomePlaceViewModel extends BaseViewModel<AddHomePlaceNavigator> {

    public AddHomePlaceViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void submitHomeAddress() {
        getNavigator().submitHomeAddress();
    }

    public void fromMap() {
        getNavigator().fromMap();
    }

    public void delete() {
        getNavigator().delete();
    }

    boolean isAllAddressFilled(String source) {
        return !TextUtils.isEmpty(source);
    }

    void addPlaceWS(Context context, String address, String lat, String lng) {

        String userid = SharedData.getString(context, Constant.USERID);

        AddPlaceMainRequest addPlaceMainRequest = new AddPlaceMainRequest();
        AddPlaceRequestData addPlaceRequestData = new AddPlaceRequestData();
        AddPlaceDatum addPlaceDatum = new AddPlaceDatum();

        addPlaceDatum.setDevicetype("Android");
        addPlaceDatum.setAddress(address);
        addPlaceDatum.setLat(lat);
        addPlaceDatum.setLng(lng);
        addPlaceDatum.setType("home");
        addPlaceDatum.setUserid(userid);

        addPlaceRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        addPlaceRequestData.setRequestType("riderPlaceEntry");
        addPlaceRequestData.setData(addPlaceDatum);

        addPlaceMainRequest.setRequestData(addPlaceRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(addPlaceMainRequest)
                .setTag("riderPlaceEntry")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(AddPlaceResponse.class, new ParsedRequestListener<AddPlaceResponse>() {

                    @Override
                    public void onResponse(AddPlaceResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
