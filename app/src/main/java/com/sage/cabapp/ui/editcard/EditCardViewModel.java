package com.sage.cabapp.ui.editcard;

import android.content.Context;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.editcard.model.EditUpdateCardDatum;
import com.sage.cabapp.ui.editcard.model.EditUpdateCardMainRequest;
import com.sage.cabapp.ui.editcard.model.EditUpdateCardRequestData;
import com.sage.cabapp.ui.editcard.model.EditUpdateCardResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class EditCardViewModel extends BaseViewModel<EditCardNavigator> {

    public EditCardViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    boolean isEnterCardNumber(String number) {
        return !TextUtils.isEmpty(number);
    }

    boolean isEnterCardName(String name) {
        return !TextUtils.isEmpty(name);
    }

    boolean isEnterCardDate(String date) {
        return !TextUtils.isEmpty(date);
    }

    boolean isEnterCardCVV(String cvv) {
        return !TextUtils.isEmpty(cvv);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void saveCard() {
        getNavigator().saveCard();
    }

    public void deleteCard() {
        getNavigator().deleteCard();
    }

    void updateCardWS(String name, String month, String year, String paymentID,Context context) {

        String userID = SharedData.getString(context, Constant.USERID);

        EditUpdateCardMainRequest editUpdateCardMainRequest = new EditUpdateCardMainRequest();
        EditUpdateCardRequestData editUpdateCardRequestData = new EditUpdateCardRequestData();
        EditUpdateCardDatum editUpdateCardDatum = new EditUpdateCardDatum();

        editUpdateCardDatum.setCardName(name);
        editUpdateCardDatum.setExpMonth(month);
        editUpdateCardDatum.setExpYear(year);
        editUpdateCardDatum.setPaymentId(paymentID);
        editUpdateCardDatum.setUserid(userID);
        editUpdateCardDatum.setDevicetype("Android");

        editUpdateCardRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        editUpdateCardRequestData.setRequestType("updateCardInfo");
        editUpdateCardRequestData.setData(editUpdateCardDatum);

        editUpdateCardMainRequest.setRequestData(editUpdateCardRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(editUpdateCardMainRequest)
                .setTag("updateCardInfo")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(EditUpdateCardResponse.class, new ParsedRequestListener<EditUpdateCardResponse>() {

                    @Override
                    public void onResponse(EditUpdateCardResponse response) {
                        getNavigator().successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().errorAPI(anError);
                    }
                });
    }
}
