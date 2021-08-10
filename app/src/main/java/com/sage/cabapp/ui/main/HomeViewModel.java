package com.sage.cabapp.ui.main;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenDatum;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenMainRequest;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenRequestData;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenResponse;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesDatum;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesMainRequest;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesRequestData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 20-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class HomeViewModel extends BaseViewModel<HomeNavigator> {
    public HomeViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void back(){
        getNavigator().backButton();
    }

    public void openMenu(){
        getNavigator().openMenu();
    }

    public void clickNavigation(){
        getNavigator().clickNavigation();
    }

    public void navClickTripHistory(){
        getNavigator().navClickTripHistory();
    }

    public void navClickPayments(){
        getNavigator().navClickPayments();
    }

    public void navClickSavedPlaces(){
        getNavigator().navClickSavedPlaces();
    }

    public void navClickPromo(){
        getNavigator().navClickPromo();
    }

    public void navClickFreeRides(){
        getNavigator().navClickFreeRides();
    }

    public void navClickHelp(){
        getNavigator().navClickHelp();
    }
    public void chooseDestination(){
        getNavigator().chooseDestination();
    }

    void updateDeviceTokenWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);
        String deviceToken = SharedData.getString(context, Constant.REFRESHED_TOKEN);

        if (deviceToken != null && !deviceToken.equalsIgnoreCase("")){

            UpdateDeviceTokenMainRequest updateDeviceTokenMainRequest = new UpdateDeviceTokenMainRequest();
            UpdateDeviceTokenRequestData updateDeviceTokenRequestData = new UpdateDeviceTokenRequestData();
            UpdateDeviceTokenDatum updateDeviceTokenDatum = new UpdateDeviceTokenDatum();

            updateDeviceTokenDatum.setUserid(userid);
            updateDeviceTokenDatum.setDevicetype("Android");
            updateDeviceTokenDatum.setDevicetoken(deviceToken);

            updateDeviceTokenRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
            updateDeviceTokenRequestData.setRequestType("updateDeviceToken");
            updateDeviceTokenRequestData.setData(updateDeviceTokenDatum);

            updateDeviceTokenMainRequest.setRequestData(updateDeviceTokenRequestData);

            AndroidNetworking.post(ApiConstants.UserRegistration)
                    .addApplicationJsonBody(updateDeviceTokenMainRequest)
                    .setTag("userRegistration")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateDeviceTokenResponse.class, new ParsedRequestListener<UpdateDeviceTokenResponse>() {

                        @Override
                        public void onResponse(UpdateDeviceTokenResponse response) {
                            getNavigator().successAPI(response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            getNavigator().errorAPI(anError);
                        }
                    });
        }

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
                        getNavigator().successSavedPlacesAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
