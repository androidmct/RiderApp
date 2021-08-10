package com.sage.cabapp.ui.tipthemmore;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.tipthemmore.model.TipThemDatum;
import com.sage.cabapp.ui.tipthemmore.model.TipThemMainRequest;
import com.sage.cabapp.ui.tipthemmore.model.TipThemRequestData;
import com.sage.cabapp.ui.tipthemmore.model.TipThemResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 08-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TipThemMoreViewModel extends BaseViewModel<TipThemMoreNavigator> {

    public TipThemMoreViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack() {
        getNavigator().onBack();
    }

    public void tipThem() {
        getNavigator().tipThem();
    }

    void tipDriverWS(Context context, String driverid, String tip, String requestID) {

        String userid = SharedData.getString(context, Constant.USERID);

        TipThemMainRequest tipThemMainRequest = new TipThemMainRequest();
        TipThemRequestData tipThemRequestData = new TipThemRequestData();
        TipThemDatum tipThemDatum = new TipThemDatum();

        tipThemDatum.setRequestId(requestID);
        tipThemDatum.setUserid(userid);
        tipThemDatum.setDriverId(driverid);
        tipThemDatum.setTripTip(tip);
        tipThemDatum.setDevicetype("Android");

        tipThemRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        tipThemRequestData.setRequestType("NewtripTipByRider");
        tipThemRequestData.setData(tipThemDatum);

        tipThemMainRequest.setRequestData(tipThemRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(tipThemMainRequest)
                .setTag("NewtripTipByRider")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(TipThemResponse.class, new ParsedRequestListener<TipThemResponse>() {

                    @Override
                    public void onResponse(TipThemResponse response) {
                        getNavigator().tipDriverResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
