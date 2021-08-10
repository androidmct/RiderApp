package com.sage.cabapp.ui.payment;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardDatum;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardMainRequest;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardRequestData;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class PaymentViewModel extends BaseViewModel<PaymentNavigator> {
    public PaymentViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void addPaymentMethod(){
        getNavigator().addPaymentMethod();
    }

    public void personalProfile(){
        getNavigator().personalProfile();
    }

    public void businessProfile(){
        getNavigator().businessProfile();
    }
    public void editbusinessProfile(){
        getNavigator().editbusinessProfile();
    }

    void getAllPaymentsCardWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);

        GetAllPaymentsCardMainRequest getAllPaymentsCardMainRequest = new GetAllPaymentsCardMainRequest();
        GetAllPaymentsCardRequestData getAllPaymentsCardRequestData = new GetAllPaymentsCardRequestData();
        GetAllPaymentsCardDatum getAllPaymentsCardDatum = new GetAllPaymentsCardDatum();

        getAllPaymentsCardDatum.setUserid(userid);
        getAllPaymentsCardDatum.setDevicetype("Android");

        getAllPaymentsCardRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        getAllPaymentsCardRequestData.setRequestType("GetALLCardDetails");
        getAllPaymentsCardRequestData.setData(getAllPaymentsCardDatum);

        getAllPaymentsCardMainRequest.setRequestData(getAllPaymentsCardRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(getAllPaymentsCardMainRequest)
                .setTag("GetALLCardDetails")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(GetAllPaymentsCardResponse.class, new ParsedRequestListener<GetAllPaymentsCardResponse>() {

                    @Override
                    public void onResponse(GetAllPaymentsCardResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }

}
