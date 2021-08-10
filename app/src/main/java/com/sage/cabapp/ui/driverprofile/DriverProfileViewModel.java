package com.sage.cabapp.ui.driverprofile;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileDatum;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileMainRequest;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileMainResponse;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileRequestData;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 24-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DriverProfileViewModel extends BaseViewModel<DriverProfileNavigator> {
    public DriverProfileViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    void getProfileDataWS(Context context) {

        String duserid = SharedData.getString(context, Constant.DRIVER_USERID);

        DriverProfileMainRequest driverProfileMainRequest = new DriverProfileMainRequest();
        DriverProfileRequestData driverProfileRequestData = new DriverProfileRequestData();
        DriverProfileDatum driverProfileDatum = new DriverProfileDatum();

        driverProfileDatum.setDriverId(duserid);
        driverProfileDatum.setDevicetype("Android");

        driverProfileRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        driverProfileRequestData.setRequestType("getDriverProfile");
        driverProfileRequestData.setData(driverProfileDatum);

        driverProfileMainRequest.setRequestData(driverProfileRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(driverProfileMainRequest)
                .setTag("getDriverProfile")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(DriverProfileMainResponse.class, new ParsedRequestListener<DriverProfileMainResponse>() {

                    @Override
                    public void onResponse(DriverProfileMainResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }
}
