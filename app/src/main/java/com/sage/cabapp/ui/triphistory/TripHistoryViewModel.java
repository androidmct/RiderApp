package com.sage.cabapp.ui.triphistory;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.triphistory.model.TripHistoryDatum;
import com.sage.cabapp.ui.triphistory.model.TripHistoryMainRequest;
import com.sage.cabapp.ui.triphistory.model.TripHistoryMainResponse;
import com.sage.cabapp.ui.triphistory.model.TripHistoryRequestData;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 07-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripHistoryViewModel extends BaseViewModel<TripHistoryNavigator> {

    public TripHistoryViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack(){
        getNavigator().back();
    }

    void tripHistoryWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);

        TripHistoryMainRequest tripHistoryMainRequest = new TripHistoryMainRequest();
        TripHistoryRequestData tripHistoryRequestData = new TripHistoryRequestData();
        TripHistoryDatum tripHistoryDatum = new TripHistoryDatum();

        tripHistoryDatum.setDevicetype("Android");
        tripHistoryDatum.setUserid(userid);

        tripHistoryRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        tripHistoryRequestData.setRequestType("riderRideHistory");
        tripHistoryRequestData.setData(tripHistoryDatum);

        tripHistoryMainRequest.setRequestData(tripHistoryRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(tripHistoryMainRequest)
                .setTag("riderRideHistory")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(TripHistoryMainResponse.class, new ParsedRequestListener<TripHistoryMainResponse>() {

                    @Override
                    public void onResponse(TripHistoryMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
