package com.sage.cabapp.ui.tripreceipt;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsDatum;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsMainRequest;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsRequestData;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoyDetailsResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripReceiptViewModel extends BaseViewModel<TripReceiptNavigator> {

    public TripReceiptViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void tipThemMore() {
        getNavigator().tipThemMore();
    }

    void tripHistoryDetailsWS(Context context,String requestID) {

        String userid = SharedData.getString(context, Constant.USERID);

        TripHistoryDetailsMainRequest tripHistoryDetailsMainRequest = new TripHistoryDetailsMainRequest();
        TripHistoryDetailsRequestData tripHistoryDetailsRequestData = new TripHistoryDetailsRequestData();
        TripHistoryDetailsDatum tripHistoryDetailsDatum = new TripHistoryDetailsDatum();

        tripHistoryDetailsDatum.setDevicetype("Android");
        tripHistoryDetailsDatum.setUserid(userid);
        tripHistoryDetailsDatum.setRequestId(requestID);

        tripHistoryDetailsRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        tripHistoryDetailsRequestData.setRequestType("riderGetTripDetails");
        tripHistoryDetailsRequestData.setData(tripHistoryDetailsDatum);

        tripHistoryDetailsMainRequest.setRequestData(tripHistoryDetailsRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(tripHistoryDetailsMainRequest)
                .setTag("riderGetTripDetails")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(TripHistoyDetailsResponse.class, new ParsedRequestListener<TripHistoyDetailsResponse>() {

                    @Override
                    public void onResponse(TripHistoyDetailsResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
