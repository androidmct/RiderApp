package com.sage.cabapp.ui.setaddress;

import android.content.Context;
import android.text.TextUtils;

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
 * Created by Maroof Ahmed Siddique on 25-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SetAddressViewModel extends BaseViewModel<SetAddressNavigator> {

    public SetAddressViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack() {
        getNavigator().onBack();
    }
    public void clickHome() {
        getNavigator().clickHome();
    }
    public void clickWork() {
        getNavigator().clickWork();
    }

    public void addMoreStopClick() {
        getNavigator().addMoreStopClick();
    }

    public void cancelStopClick() {
        getNavigator().cancelStopClick();
    }

    public void mapClick() {
        getNavigator().mapClick();
    }

    public void confirmSetAddress() {
        getNavigator().confirmSetAddress();
    }

    boolean isAllAddressFilled(String source, String destination, String addstop) {
        return !TextUtils.isEmpty(source) && !TextUtils.isEmpty(destination) && !TextUtils.isEmpty(addstop);
    }

    boolean isSDAddressFilled(String source, String destination) {
        return !TextUtils.isEmpty(source) && !TextUtils.isEmpty(destination);
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
