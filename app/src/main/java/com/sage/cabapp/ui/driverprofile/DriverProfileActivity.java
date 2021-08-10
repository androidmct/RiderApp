package com.sage.cabapp.ui.driverprofile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityDriverProfileBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileData;
import com.sage.cabapp.ui.driverprofile.model.DriverProfileMainResponse;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 24-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DriverProfileActivity extends BaseActivity<ActivityDriverProfileBinding, DriverProfileViewModel> implements DriverProfileNavigator {

    @Inject
    ViewModelProviderFactory factory;
    DriverProfileViewModel driverProfileViewModel;
    ActivityDriverProfileBinding activityDriverProfileBinding;

    @Override
    public int getBindingVariable() {
        return BR.driverProfileViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_profile;
    }

    @Override
    public DriverProfileViewModel getViewModel() {
        driverProfileViewModel = ViewModelProviders.of(this, factory).get(DriverProfileViewModel.class);
        return driverProfileViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDriverProfileBinding = getViewDataBinding();
        driverProfileViewModel.setNavigator(this);

        String driverID = SharedData.getString(this, Constant.DRIVER_USERID);

        if (driverID != null && !driverID.equalsIgnoreCase("")) {
            if (isNetworkConnected()) {
                showLoading("");
                driverProfileViewModel.getProfileDataWS(this);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        } else {
            finish();
            slideLeftToRight();
        }
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void successAPI(DriverProfileMainResponse driverProfileMainResponse) {

        hideLoading();
        try {
            if (driverProfileMainResponse.getStatus() != null && driverProfileMainResponse.getStatus().equalsIgnoreCase("true")) {

                if (driverProfileMainResponse.getData() != null) {
                    DriverProfileData driverProfileData = driverProfileMainResponse.getData();

                    Glide.with(this).load(driverProfileData.getProfileImage()).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityDriverProfileBinding.driverImage);

                    activityDriverProfileBinding.driverName.setText(driverProfileData.getDriverName());
                    activityDriverProfileBinding.driverAddress.setText(driverProfileData.getAddress());
                    activityDriverProfileBinding.vehicleInfo.setText(String.format("%s %s", driverProfileData.getVehicleMake(), driverProfileData.getVehicleModel()));

                    activityDriverProfileBinding.noOfTrips.setText(String.valueOf(driverProfileData.getTripComplete()));
                    activityDriverProfileBinding.driverRating.setText(String.valueOf(driverProfileData.getRating()));
                    activityDriverProfileBinding.driverExp.setText(driverProfileData.getDriverExp());

                } else {
                    vibrate();
                    Constant.showErrorToast(driverProfileMainResponse.getMessage(), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(driverProfileMainResponse.getMessage(), this);
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
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }
}
