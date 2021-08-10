package com.sage.cabapp.ui.tripfare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityTripFareBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIData;
import com.sage.cabapp.ui.tripfare.model.ConstantAPIResponse;
import com.sage.cabapp.utils.Constant;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripFareActivity extends BaseActivity<ActivityTripFareBinding, TripFareViewModel> implements TripFareNavigator {

    @Inject
    ViewModelProviderFactory factory;
    TripFareViewModel tripFareViewModel;
    ActivityTripFareBinding activityTripFareBinding;

    @Override
    public int getBindingVariable() {
        return BR.tripFareViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trip_fare;
    }

    @Override
    public TripFareViewModel getViewModel() {
        tripFareViewModel = ViewModelProviders.of(this, factory).get(TripFareViewModel.class);
        return tripFareViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTripFareBinding = getViewDataBinding();
        tripFareViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SEATS")) {
            String SEATS = intent.getStringExtra("SEATS");
            activityTripFareBinding.maxSeats.setText(SEATS);
        }
        if (isNetworkConnected()) {
            showLoading("");
            tripFareViewModel.constantAPIWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void onCancel() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void successAPI(ConstantAPIResponse response) {
        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData() != null) {
                    ConstantAPIData constantAPIData = response.getData();

                    activityTripFareBinding.minimumFare.setText(String.format("$%s", constantAPIData.getMinimumFare()));
                    activityTripFareBinding.perMile.setText(String.format("$%s", constantAPIData.getFareMilesMoney()));
                    activityTripFareBinding.perMinute.setText(String.format("$%s", constantAPIData.getFareTimeMoney()));
                } else {
                    vibrate();
                    Constant.showErrorToast(response.getMessage(), this);
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
    public void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }
}
