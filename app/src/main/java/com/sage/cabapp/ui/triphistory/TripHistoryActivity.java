package com.sage.cabapp.ui.triphistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityTripsHistoryBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.triphistory.adapter.TripHistoryAdapter;
import com.sage.cabapp.ui.triphistory.model.TripHistoryMainResponse;
import com.sage.cabapp.ui.triphistory.model.TripHistoryResponseData;
import com.sage.cabapp.ui.tripreceipt.TripReceiptActivity;
import com.sage.cabapp.utils.Constant;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 07-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripHistoryActivity extends BaseActivity<ActivityTripsHistoryBinding, TripHistoryViewModel> implements TripHistoryNavigator, TripHistoryClick {

    @Inject
    ViewModelProviderFactory factory;
    private TripHistoryViewModel tripHistoryViewModel;
    ActivityTripsHistoryBinding activityTripsHistoryBinding;
    SkeletonScreen tripHistorySkeleton = null;

    @Override
    public int getBindingVariable() {
        return BR.tripHistoryViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trips_history;
    }

    @Override
    public TripHistoryViewModel getViewModel() {
        tripHistoryViewModel = ViewModelProviders.of(this, factory).get(TripHistoryViewModel.class);
        return tripHistoryViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityTripsHistoryBinding = getViewDataBinding();
        tripHistoryViewModel.setNavigator(this);

        if (isNetworkConnected()) {
            showLoading("");
            tripHistoryViewModel.tripHistoryWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void back() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void successAPI(TripHistoryMainResponse response) {

        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData() != null && response.getData().size() > 0) {
                    setTripHistory(response.getData());
                } else {
                    activityTripsHistoryBinding.noTrips.setVisibility(View.VISIBLE);
                    activityTripsHistoryBinding.tripsFound.setVisibility(View.GONE);
                }
            } else {
                activityTripsHistoryBinding.noTrips.setVisibility(View.VISIBLE);
                activityTripsHistoryBinding.tripsFound.setVisibility(View.GONE);
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

    private void setTripHistory(List<TripHistoryResponseData> tripHistoryResponseData) {

        activityTripsHistoryBinding.tripList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Collections.reverse(tripHistoryResponseData);

        TripHistoryAdapter tripHistoryAdapter = new TripHistoryAdapter(tripHistoryResponseData, this);
        tripHistoryAdapter.setTripHistoryClick(this);
        activityTripsHistoryBinding.tripList.setAdapter(tripHistoryAdapter);

        activityTripsHistoryBinding.noTrips.setVisibility(View.GONE);
        activityTripsHistoryBinding.tripsFound.setVisibility(View.VISIBLE);

        tripHistorySkeleton = Skeleton.bind(activityTripsHistoryBinding.tripList)
                .adapter(tripHistoryAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1500)
                .count(10)
                .load(R.layout.shimmer_trip_history)
                .show();

        activityTripsHistoryBinding.tripList.postDelayed(new Runnable() {
            @Override
            public void run() {
                tripHistorySkeleton.hide();
            }
        }, 3000);
    }

    @Override
    public void openTripHistory(TripHistoryResponseData tripHistoryResponseData) {
        Intent intent = new Intent(this, TripReceiptActivity.class);
        intent.putExtra("TRIP_DETAIL",tripHistoryResponseData);
        startActivity(intent);
        slideRightToLeft();
    }
}
