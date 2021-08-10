package com.sage.cabapp.ui.tipthemmore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityTipThemMoreBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.tipthemmore.model.TipThemResponse;
import com.sage.cabapp.ui.triphistory.model.TripHistoryResponseData;
import com.sage.cabapp.utils.Constant;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 08-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TipThemMoreActivity extends BaseActivity<ActivityTipThemMoreBinding, TipThemMoreViewModel> implements TipThemMoreNavigator {

    @Inject
    ViewModelProviderFactory factory;
    TipThemMoreViewModel tipThemMoreViewModel;
    ActivityTipThemMoreBinding activityTipThemMoreBinding;
    TripHistoryResponseData tripHistoryResponseData = null;

    @Override
    public int getBindingVariable() {
        return BR.tipMoreViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tip_them_more;
    }

    @Override
    public TipThemMoreViewModel getViewModel() {
        tipThemMoreViewModel = ViewModelProviders.of(this, factory).get(TipThemMoreViewModel.class);
        return tipThemMoreViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTipThemMoreBinding = getViewDataBinding();
        tipThemMoreViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TRIP_DETAIL")) {
            tripHistoryResponseData = (TripHistoryResponseData) intent.getSerializableExtra("TRIP_DETAIL");
        }

        if (tripHistoryResponseData != null) {
            activityTipThemMoreBinding.yourTotal.setText(String.format("Your total %s", tripHistoryResponseData.getRideFare()));
        } else {
            vibrate();
            Constant.showErrorToast("No Data Found", this);
            finish();
            slideLeftToRight();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void tipDriverResponse(TipThemResponse response) {

        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                Constant.showSuccessToast(response.getMessage(), this);
                finish();
                slideLeftToRight();
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

    @Override
    public void tipThem() {

        String tip = Objects.requireNonNull(activityTipThemMoreBinding.etTip.getText()).toString().trim();

        if (!tip.isEmpty()) {
            hideKeyboard();
            if (isNetworkConnected()) {
                showLoading("");
                tipThemMoreViewModel.tipDriverWS(this, tripHistoryResponseData.getDriverId(), tip, tripHistoryResponseData.getRequestId());
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        } else {
            vibrate();
            Constant.showErrorToast("Please enter some tip amount!", this);
        }
    }
}
