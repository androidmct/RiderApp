package com.sage.cabapp.ui.addrivertip;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityAddDriverTipBinding;
import com.sage.cabapp.ui.addrivertip.model.GetTripFareResponse;
import com.sage.cabapp.ui.addrivertip.model.RatingDriverResponse;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import javax.inject.Inject;

public class AddDriverTipActivity extends BaseActivity<ActivityAddDriverTipBinding, AddDriverTipViewModel> implements AddDriverTipNaviagtor {

    @Inject
    ViewModelProviderFactory factory;
    AddDriverTipViewModel addDriverTipViewModel;
    ActivityAddDriverTipBinding activityAddDriverTipBinding;

    float rating = 0;

    @Override
    public int getBindingVariable() {
        return BR.addDriverTipViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_driver_tip;
    }

    @Override
    public AddDriverTipViewModel getViewModel() {
        addDriverTipViewModel = ViewModelProviders.of(this, factory).get(AddDriverTipViewModel.class);
        return addDriverTipViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddDriverTipBinding = getViewDataBinding();
        addDriverTipViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RATING")) {
            rating = intent.getFloatExtra("RATING", 0);
        }

        activityAddDriverTipBinding.rate.setRating(rating);

        String profilePic = SharedData.getString(this, Constant.DRIVER_PIC);
        Glide.with(this).load(profilePic).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityAddDriverTipBinding.driverPic);

        String driverName = SharedData.getString(this, Constant.DRIVER_NAME);
        activityAddDriverTipBinding.driverName.setText(String.format("Add a gratitude for %s", driverName));

        if (isNetworkConnected()) {
            showLoading("");
            addDriverTipViewModel.getFareAmountWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }

        /*String RIDE_FARE = SharedData.getString(this, Constant.RIDE_FARE);
        activityAddDriverTipBinding.tripTotal.setText(String.format("Trip total $%s", RIDE_FARE));
        activityAddDriverTipBinding.yourTripFare.setText(String.format("Your trip fare was $%s", RIDE_FARE));*/
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void submitTip() {

        String tip = Objects.requireNonNull(activityAddDriverTipBinding.etTip.getText()).toString().trim();
        if (addDriverTipViewModel.isEmpty(tip)) {
            tip = "0.00";
        }

        hideKeyboard();

        if (isNetworkConnected()) {
            showLoading("");
            addDriverTipViewModel.tipDriverWS(this, String.valueOf(rating), tip);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void tipDriverResponse(RatingDriverResponse ratingDriverResponse) {

        hideLoading();
        try {
            if (ratingDriverResponse.getStatus() != null && ratingDriverResponse.getStatus().equalsIgnoreCase("true")) {

                SharedData.saveString(this, Constant.DRIVER_USERID, "");
                SharedData.saveString(this, Constant.PERM_REQUEST_ID, "");
                SharedData.saveString(this, Constant.TEMP_REQUEST_ID, "");
                SharedData.saveString(this, Constant.PERM_REQUEST_STATUS, "");
                SharedData.saveString(this, Constant.PERM_REQUEST_CURRENT_STATUS, "");
                SharedData.saveBool(this, Constant.RIDE_STARTED, false);

                openActivity(this, HomeActivity.class, false, true);
                innToOut();
            } else {
                vibrate();
                Constant.showErrorToast(ratingDriverResponse.getMessgage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    public void getRideFareResponse(GetTripFareResponse response) {

        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                activityAddDriverTipBinding.tripTotal.setText(String.format("Trip total %s", response.getData()));
                activityAddDriverTipBinding.yourTripFare.setText(String.format("Your trip fare was %s", response.getData()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

}
