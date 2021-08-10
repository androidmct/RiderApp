package com.sage.cabapp.ui.savedplaces;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesDatum;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesMainRequest;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesRequestData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class SavedPlacesViewModel extends BaseViewModel<SavedPlacesNavigator> {
    public SavedPlacesViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void clickHome() {
        getNavigator().clickHome();
    }

    public void clickWork() {
        getNavigator().clickWork();
    }

    public void clickOtherPlaces() {
        getNavigator().clickOtherPlaces();
    }

    void getAllPlacesWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);

        GetAllPlacesMainRequest getAllPlacesMainRequest = new GetAllPlacesMainRequest();
        GetAllPlacesRequestData getAllPlacesRequestData = new GetAllPlacesRequestData();
        GetAllPlacesDatum getAllPlacesDatum = new GetAllPlacesDatum();

        getAllPlacesDatum.setDevicetype("Android");
        getAllPlacesDatum.setUserid(userid);

        getAllPlacesRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        getAllPlacesRequestData.setRequestType("riderGetAllPlace");
        getAllPlacesRequestData.setData(getAllPlacesDatum);

        getAllPlacesMainRequest.setRequestData(getAllPlacesRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(getAllPlacesMainRequest)
                .setTag("riderGetAllPlace")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(GetAllPlacesResponse.class, new ParsedRequestListener<GetAllPlacesResponse>() {

                    @Override
                    public void onResponse(GetAllPlacesResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
