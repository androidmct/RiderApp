package com.sage.cabapp.ui.addbusinessprofile;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityNewBusinessProfileBinding;
import com.sage.cabapp.ui.addbusinessprofile.adapter.NewBusinessProfileAdapter;
import com.sage.cabapp.ui.addbusinessprofile.fragment.BusinessProfileUpdatedFragment;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileResponse;
import com.sage.cabapp.ui.addbusinessprofile.model.NewBusinessProfileModel;
import com.sage.cabapp.ui.addpaymentmethod.AddPaymentMethodActivity;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodResponse;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.payment.model.AllCardsData;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;
import com.sage.cabapp.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class NewBusinessProfileActivity extends BaseActivity<ActivityNewBusinessProfileBinding, NewBusinessProfileViewModel> implements NewBusinessProfileNavigator, SelectNewCardCallback {

    @Inject
    ViewModelProviderFactory factory;
    NewBusinessProfileViewModel newBusinessProfileViewModel;
    ActivityNewBusinessProfileBinding activityNewBusinessProfileBinding;
    private String email = "";
    private String paymentID = "";
    private String brand = "";

    @Override
    public int getBindingVariable() {
        return BR.newBusinessViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_business_profile;
    }

    @Override
    public NewBusinessProfileViewModel getViewModel() {
        newBusinessProfileViewModel = ViewModelProviders.of(this, factory).get(NewBusinessProfileViewModel.class);
        return newBusinessProfileViewModel;
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewBusinessProfileBinding = getViewDataBinding();
        newBusinessProfileViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("BUSINESS_EMAIL")) {
            email = intent.getStringExtra("BUSINESS_EMAIL");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            newBusinessProfileViewModel.getAllPaymentsCardWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }


    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void addPaymentMethod() {

        openActivity(this, AddPaymentMethodActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void createBusinessProfile() {

        if (paymentID != null && !paymentID.equalsIgnoreCase("")) {

            if (isNetworkConnected()) {
                showLoading("");
                newBusinessProfileViewModel.createBusinessProfileWS(this, encodeBase64(email), paymentID, brand);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        } else {
            vibrate();
            Constant.showErrorToast("Please select one payment method", this);
        }
    }

    BusinessProfileUpdatedFragment profileUpdatedFragment = null;

    void showProfileUpdated() {

        profileUpdatedFragment = BusinessProfileUpdatedFragment.newInstance();
        profileUpdatedFragment.setCancelable(true);
        profileUpdatedFragment.show(getSupportFragmentManager(), "businessprofileupdated");

        new CountDownTimer(2000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (profileUpdatedFragment != null && profileUpdatedFragment.isVisible()) {
                    profileUpdatedFragment.dismiss();
                    profileUpdatedFragment = null;
                    this.cancel();
                }
                finish();
                slideLeftToRight();
            }
        }.start();
    }

    @Override
    public void successAPI(GetAllPaymentsCardResponse response) {
        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData().getData() != null && response.getData().getData().size() > 0) {
                    paymentCardsData(response.getData().getData());
                } else {
                    activityNewBusinessProfileBinding.cardList.setVisibility(View.GONE);
                }
            } else {
                activityNewBusinessProfileBinding.cardList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successCreateBusinessAPI(CreateBusinessProfileResponse response) {
        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (isNetworkConnected()){
                    newBusinessProfileViewModel.changePaymentMethodWS(this,"business");
                }

            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successDefaultMethodAPI(ChangeDefaultMethodResponse response) {
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")){
                showProfileUpdated();
            }else {
                Constant.showErrorToast(response.getMessage(),this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void paymentCardsData(List<AllCardsData> getAllPaymentsCardDataList) {

        List<NewBusinessProfileModel> personalProfileModelList = new ArrayList<>();

        for (int i = 0; i < getAllPaymentsCardDataList.size(); i++) {

            final AllCardsData getAllPaymentsCardData = getAllPaymentsCardDataList.get(i);

            String number = getAllPaymentsCardData.getCard().getLast4();

            if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("visa")) {
                personalProfileModelList.add(new NewBusinessProfileModel(getAllPaymentsCardData.getId(), String.format("•••• •••• •••• %s", number), getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_visa, false));
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("mastercard")) {
                personalProfileModelList.add(new NewBusinessProfileModel(getAllPaymentsCardData.getId(), String.format("•••• •••• •••• %s", number), getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_master_card, false));
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("amex")) {
                personalProfileModelList.add(new NewBusinessProfileModel(getAllPaymentsCardData.getId(), String.format("•••• •••• •••• %s", number), getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_amex, false));
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("discover")) {
                personalProfileModelList.add(new NewBusinessProfileModel(getAllPaymentsCardData.getId(), String.format("•••• •••• •••• %s", number), getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_discover, false));
            } else {
                personalProfileModelList.add(new NewBusinessProfileModel(getAllPaymentsCardData.getId(), String.format("•••• •••• •••• %s", number), getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_method, false));
            }
        }

        activityNewBusinessProfileBinding.cardList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        NewBusinessProfileAdapter personalProfileAdapter = new NewBusinessProfileAdapter(personalProfileModelList, this);
        personalProfileAdapter.setSelectNewCardCallback(this);
        activityNewBusinessProfileBinding.cardList.setAdapter(personalProfileAdapter);

        activityNewBusinessProfileBinding.cardList.setVisibility(View.VISIBLE);

        paymentID = "";
        brand = "";

    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }


    @Override
    public void changeActiveCard(NewBusinessProfileModel personalProfileModel) {
        paymentID = personalProfileModel.getPaymentId();
        brand = personalProfileModel.getCardType();
    }
}
