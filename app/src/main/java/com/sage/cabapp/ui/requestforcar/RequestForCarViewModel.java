package com.sage.cabapp.ui.requestforcar;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsDatum;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsMainRequest;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsRequestData;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsResponse;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentDatum;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentMainRequest;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentRequestData;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;
import com.sage.cabapp.ui.requestforcar.model.RequestSageDatum;
import com.sage.cabapp.ui.requestforcar.model.RequestSageMainRequest;
import com.sage.cabapp.ui.requestforcar.model.RequestSageMainResponseNew;
import com.sage.cabapp.ui.requestforcar.model.RequestSageRequestData;
import com.sage.cabapp.ui.requestforcar.model.RequestSageResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 16-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class RequestForCarViewModel extends BaseViewModel<RequestForCarNavigator> {
    public RequestForCarViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void requestSageClick() {
        getNavigator().requestSageClick();
    }

    public void clickNavigation() {
        getNavigator().clickNavigation();
    }

    public void closeCarDetails() {
        getNavigator().closeCarDetails();
    }

    public void clickTripFare() {
        getNavigator().clickTripFare();
    }

    public void clickPaymentMethod() {
        getNavigator().clickPaymentMethod();
    }

    void requestSageWS(String sAddress, String dAddress, String addStopAddress,
                       LatLng sourceAddressLatLng, LatLng destinationAddressLatLng, LatLng addStopAddressLatLng, LatLng currentLatLng
            , String devicetoken, String userid, String carType) {

        RequestSageMainRequest requestSageMainRequest = new RequestSageMainRequest();
        RequestSageRequestData requestSageRequestData = new RequestSageRequestData();
        RequestSageDatum requestSageDatum = new RequestSageDatum();

        requestSageDatum.setsAddress(sAddress);
        requestSageDatum.setdAddress(dAddress);
        requestSageDatum.setExAddress(addStopAddress);

        requestSageDatum.setsLat(String.valueOf(sourceAddressLatLng.latitude));
        requestSageDatum.setsLng(String.valueOf(sourceAddressLatLng.longitude));

        requestSageDatum.setdLat(String.valueOf(destinationAddressLatLng.latitude));
        requestSageDatum.setdLng(String.valueOf(destinationAddressLatLng.longitude));

        if (addStopAddressLatLng != null) {
            requestSageDatum.setExLat(String.valueOf(addStopAddressLatLng.latitude));
            requestSageDatum.setExLng(String.valueOf(addStopAddressLatLng.longitude));
        } else {
            requestSageDatum.setExLat("");
            requestSageDatum.setExLng("");
        }

        if (currentLatLng != null) {
            requestSageDatum.setcLat(String.valueOf(currentLatLng.latitude));
            requestSageDatum.setcLng(String.valueOf(currentLatLng.longitude));
        } else {
            requestSageDatum.setcLat("");
            requestSageDatum.setcLng("");
        }

        requestSageDatum.setcAddress(sAddress);

        requestSageDatum.setVehicleType(carType);
        requestSageDatum.setCardType("card");

        requestSageDatum.setUserid(userid);
        requestSageDatum.setDevicetoken(devicetoken);
        requestSageDatum.setDevicetype("Android");

        requestSageRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
       // requestSageRequestData.setRequestType("requestDriverList");
        requestSageRequestData.setRequestType("driverListDestination");
        requestSageRequestData.setData(requestSageDatum);

        requestSageMainRequest.setData(requestSageRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(requestSageMainRequest)
                .setTag("requestDriverList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(RequestSageMainResponseNew.class, new ParsedRequestListener<RequestSageMainResponseNew>() {

                    @Override
                    public void onResponse(RequestSageMainResponseNew response) {
                        getNavigator().successRequestSageNew(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }


    void getAllCars(Context context,String sLat,String sLng,String dLat,String dLng,String exLat,String exLng) {

        GetAllCarsMainRequest getAllCarsMainRequest = new GetAllCarsMainRequest();
        GetAllCarsRequestData getAllCarsRequestData = new GetAllCarsRequestData();
        GetAllCarsDatum getAllCarsDatum = new GetAllCarsDatum();

        getAllCarsDatum.setsLat(sLat);
        getAllCarsDatum.setsLng(sLng);
        getAllCarsDatum.setdLat(dLat);
        getAllCarsDatum.setdLng(dLng);
        getAllCarsDatum.setExLat(exLat);
        getAllCarsDatum.setExLng(exLng);
        getAllCarsDatum.setDevicetype("Android");

        getAllCarsRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        getAllCarsRequestData.setRequestType("fareCalculationCar");
        getAllCarsRequestData.setData(getAllCarsDatum);

        getAllCarsMainRequest.setRequestData(getAllCarsRequestData);

        AndroidNetworking.post(ApiConstants.carTypeInsert)
                .addApplicationJsonBody(getAllCarsMainRequest)
                .setTag("fareCalculationCar")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(GetAllCarsResponse.class, new ParsedRequestListener<GetAllCarsResponse>() {

                    @Override
                    public void onResponse(GetAllCarsResponse response) {
                        getNavigator().successAllCarsAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

    void getDefaultPaymentWS(Context context) {

        String userid = SharedData.getString(context,Constant.USERID);

        GetDefaultPaymentMainRequest getDefaultPaymentMainRequest = new GetDefaultPaymentMainRequest();
        GetDefaultPaymentRequestData getDefaultPaymentRequestData = new GetDefaultPaymentRequestData();
        GetDefaultPaymentDatum getDefaultPaymentDatum = new GetDefaultPaymentDatum();

        getDefaultPaymentDatum.setUserid(userid);
        getDefaultPaymentDatum.setDevicetype("Android");

        getDefaultPaymentRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        getDefaultPaymentRequestData.setRequestType("GetBusinessAndPersonal");
        getDefaultPaymentRequestData.setData(getDefaultPaymentDatum);

        getDefaultPaymentMainRequest.setRequestData(getDefaultPaymentRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(getDefaultPaymentMainRequest)
                .setTag("GetBusinessAndPersonal")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GetDefaultPaymentResponse.class, new ParsedRequestListener<GetDefaultPaymentResponse>() {

                    @Override
                    public void onResponse(GetDefaultPaymentResponse response) {
                        getNavigator().successPaymentsAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }

}
