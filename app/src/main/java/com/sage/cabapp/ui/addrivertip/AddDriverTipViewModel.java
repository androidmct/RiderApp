package com.sage.cabapp.ui.addrivertip;

import android.content.Context;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.addrivertip.model.GetTripFareRequest;
import com.sage.cabapp.ui.addrivertip.model.GetTripFareResponse;
import com.sage.cabapp.ui.addrivertip.model.RatingDriverDatum;
import com.sage.cabapp.ui.addrivertip.model.RatingDriverMainRequest;
import com.sage.cabapp.ui.addrivertip.model.RatingDriverRequestData;
import com.sage.cabapp.ui.addrivertip.model.RatingDriverResponse;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

public class AddDriverTipViewModel extends BaseViewModel<AddDriverTipNaviagtor> {

    public AddDriverTipViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void submitTip() {
        getNavigator().submitTip();
    }

    boolean isEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    void tipDriverWS(Context context, String rating, String tip) {

        String requestID = SharedData.getString(context, Constant.PERM_REQUEST_ID);
        String userid = SharedData.getString(context, Constant.USERID);
        String driverid = SharedData.getString(context, Constant.DRIVER_USERID);

        RatingDriverMainRequest ratingDriverMainRequest = new RatingDriverMainRequest();
        RatingDriverRequestData ratingDriverRequestData = new RatingDriverRequestData();
        RatingDriverDatum ratingDriverDatum = new RatingDriverDatum();

        ratingDriverDatum.setRequestId(requestID);
        ratingDriverDatum.setUserid(userid);
        ratingDriverDatum.setDriverId(driverid);
        ratingDriverDatum.setTripRating(rating);
        ratingDriverDatum.setTripTip(tip);
        ratingDriverDatum.setDevicetype("Android");

        ratingDriverRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        ratingDriverRequestData.setRequestType("riderRatingDriver");
        ratingDriverRequestData.setData(ratingDriverDatum);

        ratingDriverMainRequest.setRequestData(ratingDriverRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(ratingDriverMainRequest)
                .setTag("riderRatingDriver")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(RatingDriverResponse.class, new ParsedRequestListener<RatingDriverResponse>() {

                    @Override
                    public void onResponse(RatingDriverResponse response) {
                        getNavigator().tipDriverResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void getFareAmountWS(Context context) {

        String requestID = SharedData.getString(context, Constant.PERM_REQUEST_ID);

        GetTripFareRequest getTripFareRequest = new GetTripFareRequest();
        getTripFareRequest.setRequestId(requestID);

        AndroidNetworking.post(ApiConstants.tripFareNew)
                .addApplicationJsonBody(getTripFareRequest)
                .setTag("tripFareNew")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(GetTripFareResponse.class, new ParsedRequestListener<GetTripFareResponse>() {

                    @Override
                    public void onResponse(GetTripFareResponse response) {
                        getNavigator().getRideFareResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
