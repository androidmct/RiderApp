package com.sage.cabapp.ui.savedplaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.google.android.gms.maps.model.LatLng;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivitySavedPlacesBinding;
import com.sage.cabapp.ui.addhomeplace.AddHomePlaceActivity;
import com.sage.cabapp.ui.addworkplace.AddWorkPlaceActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;
import com.sage.cabapp.utils.Constant;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class SavedPlacesActivity extends BaseActivity<ActivitySavedPlacesBinding, SavedPlacesViewModel> implements SavedPlacesNavigator {

    @Inject
    ViewModelProviderFactory factory;
    SavedPlacesViewModel savedPlacesViewModel;
    ActivitySavedPlacesBinding activitySavedPlacesBinding;

    private boolean homeFound = false;
    private boolean workFound = false;
    private LatLng homeLatLng = null;
    private LatLng workLatLng = null;


    @Override
    public int getBindingVariable() {
        return BR.savedPlacesViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_saved_places;
    }

    @Override
    public SavedPlacesViewModel getViewModel() {
        savedPlacesViewModel = ViewModelProviders.of(this, factory).get(SavedPlacesViewModel.class);
        return savedPlacesViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySavedPlacesBinding = getViewDataBinding();
        savedPlacesViewModel.setNavigator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            savedPlacesViewModel.getAllPlacesWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void clickHome() {

        String address = "";
        if (homeFound) {
            address = activitySavedPlacesBinding.actualHomeAddress.getText().toString().trim();
        }
        Intent intent = new Intent(this, AddHomePlaceActivity.class);
        intent.putExtra("IS_ADDED", homeFound);
        intent.putExtra("ADDRESS", address);
        intent.putExtra("homeLatLng", homeLatLng);
        startActivity(intent);
        slideRightToLeft();
    }

    @Override
    public void clickWork() {
        String address = "";
        if (workFound) {
            address = activitySavedPlacesBinding.actualWorkAddress.getText().toString().trim();
        }
        Intent intent = new Intent(this, AddWorkPlaceActivity.class);
        intent.putExtra("IS_ADDED", workFound);
        intent.putExtra("ADDRESS", address);
        intent.putExtra("workLatLng", workLatLng);
        startActivity(intent);
        slideRightToLeft();
    }

    @Override
    public void clickOtherPlaces() {
        vibrate();
        Constant.showInfoToast(getResources().getString(R.string.upcoming_feature), this);
    }

    @Override
    public void successAPI(GetAllPlacesResponse getAllPlacesResponse) {

        hideLoading();
        try {

            homeFound = false;
            workFound = false;

            if (getAllPlacesResponse.getStatus() != null && getAllPlacesResponse.getStatus().equalsIgnoreCase("true")) {
                if (getAllPlacesResponse.getData() != null && getAllPlacesResponse.getData().size() > 0) {

                    for (int i = 0; i < getAllPlacesResponse.getData().size(); i++) {
                        GetAllPlacesData getAllPlacesData = getAllPlacesResponse.getData().get(i);

                        if (getAllPlacesData.getType().equalsIgnoreCase("home")) {
                            activitySavedPlacesBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_green);

                            activitySavedPlacesBinding.homeAddress.setText("Home");
                            activitySavedPlacesBinding.actualHomeAddress.setText(getAllPlacesData.getAddress());
                            activitySavedPlacesBinding.actualHomeAddress.setVisibility(View.VISIBLE);

                            double lat = Double.parseDouble(getAllPlacesData.getLat());
                            double lng = Double.parseDouble(getAllPlacesData.getLng());

                            homeLatLng = new LatLng(lat, lng);

                            homeFound = true;
                        } else if (getAllPlacesData.getType().equalsIgnoreCase("work")) {
                            activitySavedPlacesBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_green);

                            activitySavedPlacesBinding.workAddress.setText("Work");
                            activitySavedPlacesBinding.actualWorkAddress.setText(getAllPlacesData.getAddress());
                            activitySavedPlacesBinding.actualWorkAddress.setVisibility(View.VISIBLE);

                            double lat = Double.parseDouble(getAllPlacesData.getLat());
                            double lng = Double.parseDouble(getAllPlacesData.getLng());
                            workLatLng = new LatLng(lat, lng);

                            workFound = true;
                        }
                    }

                    if (!homeFound) {
                        activitySavedPlacesBinding.homeAddress.setText("Add home");
                        activitySavedPlacesBinding.actualHomeAddress.setVisibility(View.GONE);
                        activitySavedPlacesBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                    } else if (!workFound) {
                        activitySavedPlacesBinding.workAddress.setText("Add work");
                        activitySavedPlacesBinding.actualWorkAddress.setVisibility(View.GONE);
                        activitySavedPlacesBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
                    }

                } else {
                    activitySavedPlacesBinding.homeAddress.setText("Add home");
                    activitySavedPlacesBinding.actualHomeAddress.setVisibility(View.GONE);

                    activitySavedPlacesBinding.workAddress.setText("Add work");
                    activitySavedPlacesBinding.actualWorkAddress.setVisibility(View.GONE);

                    activitySavedPlacesBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                    activitySavedPlacesBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
                }
            }else {
                activitySavedPlacesBinding.homeAddress.setText("Add home");
                activitySavedPlacesBinding.actualHomeAddress.setVisibility(View.GONE);

                activitySavedPlacesBinding.workAddress.setText("Add work");
                activitySavedPlacesBinding.actualWorkAddress.setVisibility(View.GONE);

                activitySavedPlacesBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                activitySavedPlacesBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
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
        slideTopToBottom();
    }
}
