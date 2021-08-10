package com.sage.cabapp.ui.addbusinessprofile;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileDatum;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileMainRequest;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileRequestData;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileResponse;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodRequest;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodResponse;
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
public class NewBusinessProfileViewModel extends BaseViewModel<NewBusinessProfileNavigator> {

    public NewBusinessProfileViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void addPaymentMethod() {
        getNavigator().addPaymentMethod();
    }

    public void createBusinessProfile() {
        getNavigator().createBusinessProfile();
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


    void createBusinessProfileWS(Context context, String businessemail, String paymentId, String brand) {

        String userid = SharedData.getString(context, Constant.USERID);

        CreateBusinessProfileMainRequest createBusinessProfileMainRequest = new CreateBusinessProfileMainRequest();
        CreateBusinessProfileRequestData createBusinessProfileRequestData = new CreateBusinessProfileRequestData();
        CreateBusinessProfileDatum createBusinessProfileDatum = new CreateBusinessProfileDatum();

        createBusinessProfileDatum.setUserid(userid);
        createBusinessProfileDatum.setBusinessemail(businessemail);
        createBusinessProfileDatum.setPaymentId(paymentId);
        createBusinessProfileDatum.setBrand(brand);
        createBusinessProfileDatum.setDefaultActive("true");
        createBusinessProfileDatum.setDevicetype("Android");

        createBusinessProfileRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        createBusinessProfileRequestData.setRequestType("BusinessPaymentRegister");
        createBusinessProfileRequestData.setData(createBusinessProfileDatum);

        createBusinessProfileMainRequest.setRequestData(createBusinessProfileRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(createBusinessProfileMainRequest)
                .setTag("BusinessPaymentRegister")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CreateBusinessProfileResponse.class, new ParsedRequestListener<CreateBusinessProfileResponse>() {

                    @Override
                    public void onResponse(CreateBusinessProfileResponse response) {
                        getNavigator().successCreateBusinessAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }

    void changePaymentMethodWS(Context context, String defaultmethod) {

        String userid = SharedData.getString(context, Constant.USERID);

        ChangeDefaultMethodRequest changeDefaultMethodRequest = new ChangeDefaultMethodRequest();
        changeDefaultMethodRequest.setDefaultmethod(defaultmethod);
        changeDefaultMethodRequest.setUserid(userid);


        AndroidNetworking.post(ApiConstants.defaultCardType)
                .addApplicationJsonBody(changeDefaultMethodRequest)
                .setTag("defaultCardType")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(ChangeDefaultMethodResponse.class, new ParsedRequestListener<ChangeDefaultMethodResponse>() {

                    @Override
                    public void onResponse(ChangeDefaultMethodResponse response) {
                        getNavigator().successDefaultMethodAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });

    }

}
