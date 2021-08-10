package com.sage.cabapp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityPaymentBinding;
import com.sage.cabapp.ui.addbusinessprofile.AddBusinessProfileActivity;
import com.sage.cabapp.ui.addpaymentmethod.AddPaymentMethodActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.editbusinessprofile.EditBusinessProfileActivity;
import com.sage.cabapp.ui.editcard.EditCardActivity;
import com.sage.cabapp.ui.payment.adapter.PaymentCardsAdapter;
import com.sage.cabapp.ui.payment.model.AllCardsData;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;
import com.sage.cabapp.ui.personalprofile.PersonalProfileActivity;
import com.sage.cabapp.utils.Constant;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class PaymentActivity extends BaseActivity<ActivityPaymentBinding, PaymentViewModel> implements PaymentNavigator, CallbackOpenPaymentCard {

    @Inject
    ViewModelProviderFactory factory;
    PaymentViewModel paymentViewModel;
    ActivityPaymentBinding activityPaymentBinding;

    @Override
    public int getBindingVariable() {
        return BR.paymentViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public PaymentViewModel getViewModel() {
        paymentViewModel = ViewModelProviders.of(this, factory).get(PaymentViewModel.class);
        return paymentViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPaymentBinding = getViewDataBinding();
        paymentViewModel.setNavigator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            paymentViewModel.getAllPaymentsCardWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void addPaymentMethod() {
        openActivity(this, AddPaymentMethodActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void personalProfile() {
        openActivity(this, PersonalProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void businessProfile() {

        openActivity(this, AddBusinessProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void editbusinessProfile() {
        openActivity(this, EditBusinessProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void successAPI(GetAllPaymentsCardResponse response) {
        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData().getData() != null && response.getData().getData().size() > 0) {
                    paymentCardsData(response.getData().getData(), response.getData().getPersonal(), response.getData().getBusiness());
                } else {
                    activityPaymentBinding.cardList.setVisibility(View.GONE);
                }
            } else {
                activityPaymentBinding.cardList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paymentCardsData(List<AllCardsData> getAllPaymentsCardDataList, String personal, String business) {

        activityPaymentBinding.cardList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        PaymentCardsAdapter paymentCardsAdapter = new PaymentCardsAdapter(getAllPaymentsCardDataList, this);
        paymentCardsAdapter.setCallbackOpenPaymentCard(this);
        activityPaymentBinding.cardList.setAdapter(paymentCardsAdapter);

        activityPaymentBinding.cardList.setVisibility(View.VISIBLE);

        for (int i = 0; i < getAllPaymentsCardDataList.size(); i++) {

            AllCardsData allCardsData = getAllPaymentsCardDataList.get(i);

            if (personal != null && !personal.equalsIgnoreCase("")) {
                if (personal.equalsIgnoreCase(allCardsData.getId())) {
                    String capitalizeWord = "";
                    String first = allCardsData.getCard().getBrand().substring(0, 1);
                    String afterfirst = allCardsData.getCard().getBrand().substring(1);
                    capitalizeWord += first.toUpperCase() + afterfirst + " ";
                    activityPaymentBinding.personalCard.setText(String.format("%s • %s", capitalizeWord, allCardsData.getCard().getLast4()));
                }
            }

            if (business != null && !business.equalsIgnoreCase("")) {
                if (business.equalsIgnoreCase(allCardsData.getId())) {
                    String capitalizeWord = "";
                    String first = allCardsData.getCard().getBrand().substring(0, 1);
                    String afterfirst = allCardsData.getCard().getBrand().substring(1);
                    capitalizeWord += first.toUpperCase() + afterfirst + " ";
                    activityPaymentBinding.businessCard.setText(String.format("%s • %s", capitalizeWord, allCardsData.getCard().getLast4()));
                }
            }
        }

        if (business != null && !business.equalsIgnoreCase("")) {
            activityPaymentBinding.createBusinessLayout.setVisibility(View.GONE);
            activityPaymentBinding.editBusinessLayout.setVisibility(View.VISIBLE);
        } else {
            activityPaymentBinding.createBusinessLayout.setVisibility(View.VISIBLE);
            activityPaymentBinding.editBusinessLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    public void openPaymentCard(AllCardsData getAllPaymentsCardData) {
        Intent intent = new Intent(this, EditCardActivity.class);
        intent.putExtra("CARD_DETAILS", getAllPaymentsCardData);
        startActivity(intent);
        slideRightToLeft();

    }
}
