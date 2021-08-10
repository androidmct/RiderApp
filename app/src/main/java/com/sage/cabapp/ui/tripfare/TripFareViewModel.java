package com.sage.cabapp.ui.tripfare;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIDatum;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIMainRequest;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIRequestData;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripFareViewModel extends BaseViewModel<TripFareNavigator> {

    public TripFareViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onCancel(){
        getNavigator().onCancel();
    }


    void constantAPIWS(Context context) {

        ConstantAPIMainRequest constantAPIMainRequest = new ConstantAPIMainRequest();
        ConstantAPIRequestData constantAPIRequestData = new ConstantAPIRequestData();
        ConstantAPIDatum constantAPIDatum = new ConstantAPIDatum();

        constantAPIDatum.setDevicetype("Android");
        constantAPIDatum.setId("1");

        constantAPIRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        constantAPIRequestData.setRequestType("constantApi");
        constantAPIRequestData.setData(constantAPIDatum);

        constantAPIMainRequest.setRequestData(constantAPIRequestData);

        AndroidNetworking.post(ApiConstants.constantInsert)
                .addApplicationJsonBody(constantAPIMainRequest)
                .setTag("constantApi")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(ConstantAPIResponse.class, new ParsedRequestListener<ConstantAPIResponse>() {

                    @Override
                    public void onResponse(ConstantAPIResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
