package com.sage.cabapp.ui.updateroute;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.error.ANError;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityUpdateRouteBinding;
import com.sage.cabapp.ui.addhomeplace.AddHomePlaceActivity;
import com.sage.cabapp.ui.addworkplace.AddWorkPlaceActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.updateroute.fragment.UpdateRouteFragment;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedMainResponse;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedResponseData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;
import com.sage.cabapp.ui.setaddress.DestinationAddressClickCallback;
import com.sage.cabapp.ui.setaddress.SourceAddressClickCallback;
import com.sage.cabapp.ui.setaddress.adapter.DestinationPlacesAutoCompleteAdapter;
import com.sage.cabapp.ui.setaddress.adapter.SourcePlacesAutoCompleteAdapter;
import com.sage.cabapp.ui.setaddress.model.PlaceAutocomplete;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 25-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class UpdateRouteActivity extends BaseActivity<ActivityUpdateRouteBinding, UpdateRouteViewModel> implements UpdateRouteNavigator, SourceAddressClickCallback, DestinationAddressClickCallback {

    @Inject
    ViewModelProviderFactory factory;
    private UpdateRouteViewModel updateRouteViewModel;
    private ActivityUpdateRouteBinding activityUpdateRouteBinding;
    private SourcePlacesAutoCompleteAdapter sourcePlacesAutoCompleteAdapter;
    private DestinationPlacesAutoCompleteAdapter destinationPlacesAutoCompleteAdapter;
    private PlacesClient placesClient;
    private ArrayList<PlaceAutocomplete> sourcemResultList = new ArrayList<>();
    private ArrayList<PlaceAutocomplete> destinationmResultList = new ArrayList<>();
    private boolean addStop = false;
    private LatLng sourceAddressLatLng = null;
    private LatLng destinationAddressLatLng = null;
    private LatLng destinationHome = null;
    private LatLng destinationWork = null;
    private LatLng addStopAddressLatLng = null;
    private String requestID = "";
    private boolean homeFound = false;
    private boolean workFound = false;
    SkeletonScreen sourceSkeletonScreen = null;
    SkeletonScreen destinationSkeletonScreen = null;

    @Override
    public int getBindingVariable() {
        return BR.updateRouteViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_route;
    }

    @Override
    public UpdateRouteViewModel getViewModel() {
        updateRouteViewModel = ViewModelProviders.of(this, factory).get(UpdateRouteViewModel.class);
        return updateRouteViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateRouteBinding = getViewDataBinding();
        updateRouteViewModel.setNavigator(this);

        Places.initialize(this, getResources().getString(R.string.places_api_key));
        placesClient = Places.createClient(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ADDRESS")) {

            requestID = intent.getStringExtra("requestID");

            activityUpdateRouteBinding.etSourceaddress.setText(intent.getStringExtra("ADDRESS"));
            sourceAddressLatLng = intent.getParcelableExtra("LAT_LNG");

            if (intent.hasExtra("DEST_LAT_LNG")) {
                destinationAddressLatLng = intent.getParcelableExtra("DEST_LAT_LNG");
                activityUpdateRouteBinding.etDestinationaddress.setText(intent.getStringExtra("DEST_ADDRESS"));
                activityUpdateRouteBinding.etDestinationaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

            }

            if (intent.hasExtra("EX_LAT_LNG")) {
                addStopAddressLatLng = intent.getParcelableExtra("EX_LAT_LNG");
                activityUpdateRouteBinding.etDestinationaddresssecond.setText(intent.getStringExtra("EX_ADDRESS"));
                activityUpdateRouteBinding.etDestinationaddresssecond.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                addStop = true;
                activityUpdateRouteBinding.addStopLayout.setVisibility(View.VISIBLE);
                activityUpdateRouteBinding.addStopAddressLine.setVisibility(View.VISIBLE);
                activityUpdateRouteBinding.addStopView.setVisibility(View.VISIBLE);
                activityUpdateRouteBinding.addClick.setBackgroundResource(R.drawable.ic_remove_gray);
            }else {
                addStop = false;
                activityUpdateRouteBinding.addStopLayout.setVisibility(View.GONE);
                activityUpdateRouteBinding.addStopAddressLine.setVisibility(View.GONE);
                activityUpdateRouteBinding.addStopView.setVisibility(View.GONE);
                activityUpdateRouteBinding.addClick.setBackgroundResource(R.drawable.ic_add_grey);
            }

            checkFilledAddress();
        } else {
            if (isNetworkConnected()) {

                String requestID = SharedData.getString(this, Constant.PERM_REQUEST_ID);
                if (requestID != null && !requestID.equalsIgnoreCase("")) {
                    updateRouteViewModel.acceptedRequestedWS(this);
                } else {
                    vibrate();
                    Constant.showErrorToast("No request found", this);
                    finish();
                    slideLeftToRight();
                }
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        }

        activityUpdateRouteBinding.etSourceaddress.setCursorVisible(false);
        activityUpdateRouteBinding.etSourceaddress.setClickable(false);

        activityUpdateRouteBinding.sourcePlacesRecyclerView.setNestedScrollingEnabled(false);
        activityUpdateRouteBinding.destinationPlacesRecyclerView.setNestedScrollingEnabled(false);
        sourceAddressView();
        destinationAddressView();

        // activityUpdateRouteBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

        addTextWatcherSource();
        addTextWatcherDestination();
        addTextWatcherDestinationSecond();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherSource() {

        activityUpdateRouteBinding.etSourceaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityUpdateRouteBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityUpdateRouteBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityUpdateRouteBinding.etSourceaddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityUpdateRouteBinding.etSourceaddress.getRight() - activityUpdateRouteBinding.etSourceaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityUpdateRouteBinding.etSourceaddress.setText("");
                            checkFilledAddress();
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activityUpdateRouteBinding.etSourceaddress.postDelayed(() -> activityUpdateRouteBinding.etSourceaddress.setSelection(Objects.requireNonNull(activityUpdateRouteBinding.etSourceaddress.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityUpdateRouteBinding.etSourceaddress.setCursorVisible(true);

                activityUpdateRouteBinding.etDestinationaddress.setSelection(0);
                activityUpdateRouteBinding.etDestinationaddress.setCursorVisible(false);

                if (addStop) {
                    activityUpdateRouteBinding.etDestinationaddresssecond.setSelection(0);
                    activityUpdateRouteBinding.etDestinationaddresssecond.setCursorVisible(false);
                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherDestination() {

        activityUpdateRouteBinding.etDestinationaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityUpdateRouteBinding.etDestinationaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityUpdateRouteBinding.etDestinationaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityUpdateRouteBinding.etDestinationaddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityUpdateRouteBinding.etDestinationaddress.getRight() - activityUpdateRouteBinding.etDestinationaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityUpdateRouteBinding.etDestinationaddress.setText("");
                            checkFilledAddress();
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activityUpdateRouteBinding.etDestinationaddress.postDelayed(() -> activityUpdateRouteBinding.etDestinationaddress.setSelection(Objects.requireNonNull(activityUpdateRouteBinding.etDestinationaddress.getText()).toString().length()), 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityUpdateRouteBinding.etDestinationaddress.setCursorVisible(true);

                activityUpdateRouteBinding.etSourceaddress.setSelection(0);
                activityUpdateRouteBinding.etSourceaddress.setCursorVisible(false);

                if (addStop) {
                    activityUpdateRouteBinding.etDestinationaddresssecond.setSelection(0);
                    activityUpdateRouteBinding.etDestinationaddresssecond.setCursorVisible(false);
                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherDestinationSecond() {

        activityUpdateRouteBinding.etDestinationaddresssecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityUpdateRouteBinding.etDestinationaddresssecond.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityUpdateRouteBinding.etDestinationaddresssecond.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityUpdateRouteBinding.etDestinationaddresssecond.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityUpdateRouteBinding.etDestinationaddresssecond.getRight() - activityUpdateRouteBinding.etDestinationaddresssecond.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityUpdateRouteBinding.etDestinationaddresssecond.setText("");
                            checkFilledAddress();
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activityUpdateRouteBinding.etDestinationaddresssecond.postDelayed(() -> activityUpdateRouteBinding.etDestinationaddresssecond.setSelection(Objects.requireNonNull(activityUpdateRouteBinding.etDestinationaddresssecond.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityUpdateRouteBinding.etDestinationaddresssecond.setCursorVisible(true);

                activityUpdateRouteBinding.etSourceaddress.setSelection(0);
                activityUpdateRouteBinding.etSourceaddress.setCursorVisible(false);

                activityUpdateRouteBinding.etDestinationaddress.setSelection(0);
                activityUpdateRouteBinding.etDestinationaddress.setCursorVisible(false);

                return false;
            }
        });
    }

    private void sourceAddressView() {

        activityUpdateRouteBinding.etSourceaddress.addTextChangedListener(sourcefilterTextWatcher);
        sourcePlacesAutoCompleteAdapter = new SourcePlacesAutoCompleteAdapter(this);
        activityUpdateRouteBinding.sourcePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sourcePlacesAutoCompleteAdapter.setSourceAddressClickCallback(this);
        activityUpdateRouteBinding.sourcePlacesRecyclerView.setAdapter(sourcePlacesAutoCompleteAdapter);
        activityUpdateRouteBinding.sourcePlacesRecyclerView.getRecycledViewPool().clear();
        sourcePlacesAutoCompleteAdapter.notifyDataSetChanged();
    }

    private void destinationAddressView() {

        activityUpdateRouteBinding.etDestinationaddress.addTextChangedListener(destinationfilterTextWatcher);
        activityUpdateRouteBinding.etDestinationaddresssecond.addTextChangedListener(seconddestinationfilterTextWatcher);
        destinationPlacesAutoCompleteAdapter = new DestinationPlacesAutoCompleteAdapter(this);
        activityUpdateRouteBinding.destinationPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        destinationPlacesAutoCompleteAdapter.setDestinationAddressClickCallback(this);
        activityUpdateRouteBinding.destinationPlacesRecyclerView.setAdapter(destinationPlacesAutoCompleteAdapter);
        activityUpdateRouteBinding.destinationPlacesRecyclerView.getRecycledViewPool().clear();
        destinationPlacesAutoCompleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void addMoreStopClick() {

        if (activityUpdateRouteBinding.addStopLayout.getVisibility() == View.GONE) {

            addStop = true;
            activityUpdateRouteBinding.addStopLayout.setVisibility(View.VISIBLE);
            activityUpdateRouteBinding.addStopAddressLine.setVisibility(View.VISIBLE);
            activityUpdateRouteBinding.addStopView.setVisibility(View.VISIBLE);
            activityUpdateRouteBinding.addClick.setBackgroundResource(R.drawable.ic_remove_gray);
        } else {

            addStop = false;
            activityUpdateRouteBinding.addStopLayout.setVisibility(View.GONE);
            activityUpdateRouteBinding.addStopAddressLine.setVisibility(View.GONE);
            activityUpdateRouteBinding.addStopView.setVisibility(View.GONE);
            activityUpdateRouteBinding.addClick.setBackgroundResource(R.drawable.ic_add_grey);
        }

        activityUpdateRouteBinding.etDestinationaddresssecond.setText("");

        checkFilledAddress();
    }

    @Override
    public void cancelStopClick() {

        addStop = false;
        activityUpdateRouteBinding.addStopLayout.setVisibility(View.GONE);
        activityUpdateRouteBinding.etDestinationaddresssecond.setText("");
        activityUpdateRouteBinding.addStopAddressLine.setVisibility(View.GONE);
        activityUpdateRouteBinding.addStopView.setVisibility(View.GONE);
        activityUpdateRouteBinding.addClick.setBackgroundResource(R.drawable.ic_add_grey);

        checkFilledAddress();
    }

    @Override
    public void clickHome() {

        if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
        }

        if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        }

        String address = "";
        if (homeFound) {
            address = activityUpdateRouteBinding.actualHomeAddress.getText().toString().trim();
            activityUpdateRouteBinding.etDestinationaddress.setText(address);
            destinationAddressLatLng = destinationHome;
            checkFilledAddress();
            return;
        }
        Intent intent = new Intent(this, AddHomePlaceActivity.class);
        intent.putExtra("IS_ADDED", homeFound);
        intent.putExtra("ADDRESS", address);
        startActivity(intent);
        slideRightToLeft();
    }

    @Override
    public void clickWork() {

        if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
        }

        if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        }

        String address = "";
        if (workFound) {
            address = activityUpdateRouteBinding.actualWorkAddress.getText().toString().trim();
            activityUpdateRouteBinding.etDestinationaddress.setText(address);
            destinationAddressLatLng = destinationWork;
            checkFilledAddress();
            return;
        }
        Intent intent = new Intent(this, AddWorkPlaceActivity.class);
        intent.putExtra("IS_ADDED", workFound);
        intent.putExtra("ADDRESS", address);
        startActivity(intent);
        slideRightToLeft();
    }

    @Override
    public void confirmSetAddress() {


        String source = Objects.requireNonNull(activityUpdateRouteBinding.etSourceaddress.getText()).toString().trim();
        String destination = Objects.requireNonNull(activityUpdateRouteBinding.etDestinationaddress.getText()).toString().trim();
        String addstop = Objects.requireNonNull(activityUpdateRouteBinding.etDestinationaddresssecond.getText()).toString().trim();

        if (TextUtils.isEmpty(addstop)) {
            addStopAddressLatLng = null;
        }

        Bundle bundle = new Bundle();
        bundle.putString("SOURCE_ADD", source);
        bundle.putString("DEST_ADD", destination);
        bundle.putString("ADDSTOP_ADD", addstop);
        bundle.putString("requestID", requestID);
        bundle.putParcelable("SOURCE_LATLNG", sourceAddressLatLng);
        bundle.putParcelable("DEST_LATLNG", destinationAddressLatLng);
        bundle.putParcelable("ADDSTOP_LATLNG", addStopAddressLatLng);

        UpdateRouteFragment updateRouteFragment = UpdateRouteFragment.newInstance();
        updateRouteFragment.setArguments(bundle);
        updateRouteFragment.setCancelable(false);
        updateRouteFragment.show(getSupportFragmentManager(), "updateRouteFragment");
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
                            activityUpdateRouteBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_green);

                            activityUpdateRouteBinding.homeAddress.setText("Home");
                            activityUpdateRouteBinding.actualHomeAddress.setText(getAllPlacesData.getAddress());
                            activityUpdateRouteBinding.actualHomeAddress.setVisibility(View.VISIBLE);

                            destinationHome = new LatLng(Double.parseDouble(getAllPlacesData.getLat()), Double.parseDouble(getAllPlacesData.getLng()));
                            homeFound = true;
                        } else if (getAllPlacesData.getType().equalsIgnoreCase("work")) {
                            activityUpdateRouteBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_green);

                            activityUpdateRouteBinding.workAddress.setText("Work");
                            activityUpdateRouteBinding.actualWorkAddress.setText(getAllPlacesData.getAddress());
                            activityUpdateRouteBinding.actualWorkAddress.setVisibility(View.VISIBLE);
                            destinationWork = new LatLng(Double.parseDouble(getAllPlacesData.getLat()), Double.parseDouble(getAllPlacesData.getLng()));

                            workFound = true;
                        }
                    }

                    if (!homeFound) {
                        activityUpdateRouteBinding.homeAddress.setText("Add home");
                        activityUpdateRouteBinding.actualHomeAddress.setVisibility(View.GONE);
                        activityUpdateRouteBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                    } else if (!workFound) {
                        activityUpdateRouteBinding.workAddress.setText("Add work");
                        activityUpdateRouteBinding.actualWorkAddress.setVisibility(View.GONE);
                        activityUpdateRouteBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
                    }

                } else {
                    activityUpdateRouteBinding.homeAddress.setText("Add home");
                    activityUpdateRouteBinding.actualHomeAddress.setVisibility(View.GONE);

                    activityUpdateRouteBinding.workAddress.setText("Add work");
                    activityUpdateRouteBinding.actualWorkAddress.setVisibility(View.GONE);

                    activityUpdateRouteBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                    activityUpdateRouteBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.GONE && activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.GONE) {
            activityUpdateRouteBinding.placesLayout.setVisibility(View.VISIBLE);
        }

        checkFilledAddress();
    }

    @Override
    public void sageAcceptedResponse(RideAcceptedMainResponse response) {
        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getRequestData() != null && response.getDriverData() != null) {

                    RideAcceptedResponseData rideAcceptedResponseData = response.getRequestData();

                    requestID = rideAcceptedResponseData.getRequestId();

                    activityUpdateRouteBinding.etSourceaddress.setText(rideAcceptedResponseData.getSAddress());
                    activityUpdateRouteBinding.etDestinationaddress.setText(rideAcceptedResponseData.getDAddress());
                    activityUpdateRouteBinding.etDestinationaddresssecond.setText(rideAcceptedResponseData.getExAddress());

                    checkFilledAddress();

                    double slat = Double.parseDouble(rideAcceptedResponseData.getSLat());
                    double slng = Double.parseDouble(rideAcceptedResponseData.getSLng());
                    sourceAddressLatLng = new LatLng(slat, slng);

                    double dlat = Double.parseDouble(rideAcceptedResponseData.getDLat());
                    double dlng = Double.parseDouble(rideAcceptedResponseData.getDLng());
                    destinationAddressLatLng = new LatLng(dlat, dlng);

                    double exlat = Double.parseDouble(rideAcceptedResponseData.getExLat());
                    double exlng = Double.parseDouble(rideAcceptedResponseData.getExLng());
                    addStopAddressLatLng = new LatLng(exlat, exlng);

                } else {
                    Constant.showErrorToast(response.getMessage(), this);
                }
            } else {
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            updateRouteViewModel.getAllPlacesWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            if (resultCode == Activity.RESULT_OK) {
                String address = Objects.requireNonNull(data).getStringExtra("address");
                sourceAddressLatLng = Objects.requireNonNull(data).getParcelableExtra("tapLocation");
                activityUpdateRouteBinding.etSourceaddress.setText(address);
                activityUpdateRouteBinding.etSourceaddress.setSelection(0);
                activityUpdateRouteBinding.etSourceaddress.setCursorVisible(false);

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    private TextWatcher sourcefilterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                getFilterSource().filter(s.toString());
                if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.GONE) {
                    activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                addSourceSkeleton();

                if (activityUpdateRouteBinding.placesLayout.getVisibility() == View.VISIBLE) {
                    activityUpdateRouteBinding.placesLayout.setVisibility(View.GONE);
                }
            } else {
                if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
                }

                if (activityUpdateRouteBinding.placesLayout.getVisibility() == View.GONE) {
                    activityUpdateRouteBinding.placesLayout.setVisibility(View.VISIBLE);
                }
            }

            if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            }


        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void addSourceSkeleton() {

        sourceSkeletonScreen = Skeleton.bind(activityUpdateRouteBinding.sourcePlacesRecyclerView)
                .adapter(sourcePlacesAutoCompleteAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1500)
                .count(10)
                .load(R.layout.shimmer_set_address)
                .show();

        activityUpdateRouteBinding.sourcePlacesRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sourceSkeletonScreen.hide();
            }
        }, 1500);
    }

    private void destinationSkeleton() {

        destinationSkeletonScreen = Skeleton.bind(activityUpdateRouteBinding.destinationPlacesRecyclerView)
                .adapter(destinationPlacesAutoCompleteAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1500)
                .count(10)
                .load(R.layout.shimmer_set_address)
                .show();
    }

    public Filter getFilterSource() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.

                    if (sourcemResultList != null && sourcemResultList.size() > 0) {
                        sourcemResultList.clear();
                        // sourcePlacesAutoCompleteAdapter.notifyDataSetChanged();
                    }
                    sourcemResultList = getPredictions(constraint);

                    if (sourcemResultList != null && sourcemResultList.size() > 0) {
                        // The API successfully returned results.
                        results.values = sourcemResultList;
                        results.count = sourcemResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.

                    if (sourcemResultList != null && sourcemResultList.size() > 0) {
                        sourcePlacesAutoCompleteAdapter.addAll(sourcemResultList);
                    }

                    sourceSkeletonScreen.hide();
                    sourcePlacesAutoCompleteAdapter.notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        };
    }

    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {

        final ArrayList<PlaceAutocomplete> resultList = new ArrayList<>();

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                //.setCountry("BD")
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        if (autocompletePredictions.isSuccessful()) {
            FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = autocompletePredictions.getResult();
            if (findAutocompletePredictionsResponse != null)
                for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                    resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getPrimaryText(new StyleSpan(Typeface.NORMAL)).toString(), prediction.getFullText(new StyleSpan(Typeface.BOLD)).toString()));
                }

            return resultList;
        } else {
            return resultList;
        }
    }

    private TextWatcher destinationfilterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {

            addStop = false;

            if (!s.toString().equals("")) {
                getFilterDestination().filter(s.toString());
                if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.GONE) {
                    activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                destinationSkeleton();

                if (activityUpdateRouteBinding.placesLayout.getVisibility() == View.VISIBLE) {
                    activityUpdateRouteBinding.placesLayout.setVisibility(View.GONE);
                }
            } else {
                if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
                }

                if (activityUpdateRouteBinding.placesLayout.getVisibility() == View.GONE) {
                    activityUpdateRouteBinding.placesLayout.setVisibility(View.VISIBLE);
                }
            }

            if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private TextWatcher seconddestinationfilterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {

            addStop = true;

            if (!s.toString().equals("")) {
                getFilterDestination().filter(s.toString());
                if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.GONE) {
                    activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                destinationSkeleton();
            } else {
                if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
                }
            }

            if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public Filter getFilterDestination() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.

                    if (destinationmResultList != null && destinationmResultList.size() > 0) {
                        destinationmResultList.clear();
                        // sourcePlacesAutoCompleteAdapter.notifyDataSetChanged();
                    }
                    destinationmResultList = getPredictions(constraint);

                    if (destinationmResultList != null && destinationmResultList.size() > 0) {
                        // The API successfully returned results.
                        results.values = destinationmResultList;
                        results.count = destinationmResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.

                    if (destinationmResultList != null && destinationmResultList.size() > 0) {
                        destinationPlacesAutoCompleteAdapter.addAll(destinationmResultList);
                    }

                    destinationSkeletonScreen.hide();
                    destinationPlacesAutoCompleteAdapter.notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        };
    }

    @Override
    public void sourceAddressClick(PlaceAutocomplete place) {
        String placeId = String.valueOf(place.placeId);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place1 = response.getPlace();

            sourceAddressLatLng = place1.getLatLng();

            activityUpdateRouteBinding.etSourceaddress.setText(place1.getName());
            activityUpdateRouteBinding.etSourceaddress.setSelection(Objects.requireNonNull(activityUpdateRouteBinding.etSourceaddress.getText()).length());


            if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activityUpdateRouteBinding.placesLayout.getVisibility() == View.GONE) {
                activityUpdateRouteBinding.placesLayout.setVisibility(View.VISIBLE);
            }

            checkFilledAddress();

        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                Toast.makeText(getApplicationContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void destinationAddressClick(PlaceAutocomplete place) {
        String placeId = String.valueOf(place.placeId);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place1 = response.getPlace();
            if (addStop) {

                addStopAddressLatLng = place1.getLatLng();

                activityUpdateRouteBinding.etDestinationaddresssecond.setText(place1.getName());
                activityUpdateRouteBinding.etDestinationaddresssecond.setSelection(0);
                activityUpdateRouteBinding.etDestinationaddresssecond.setCursorVisible(false);

            } else {

                destinationAddressLatLng = place1.getLatLng();

                activityUpdateRouteBinding.etDestinationaddress.setText(place1.getName());
                activityUpdateRouteBinding.etDestinationaddress.setSelection(0);
                activityUpdateRouteBinding.etDestinationaddress.setCursorVisible(false);
            }

            if (activityUpdateRouteBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activityUpdateRouteBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activityUpdateRouteBinding.placesLayout.getVisibility() == View.GONE) {
                activityUpdateRouteBinding.placesLayout.setVisibility(View.VISIBLE);
            }

            checkFilledAddress();

        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                Toast.makeText(getApplicationContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFilledAddress() {
        String source = Objects.requireNonNull(activityUpdateRouteBinding.etSourceaddress.getText()).toString().trim();
        String destination = Objects.requireNonNull(activityUpdateRouteBinding.etDestinationaddress.getText()).toString().trim();
        String addstop = Objects.requireNonNull(activityUpdateRouteBinding.etDestinationaddresssecond.getText()).toString().trim();

        if (updateRouteViewModel.isAllAddressFilled(source, destination, addstop) && sourceAddressLatLng != null && destinationAddressLatLng != null && addStopAddressLatLng != null) {
            activityUpdateRouteBinding.confirm.setVisibility(View.VISIBLE);
            activityUpdateRouteBinding.appreciateLayout.setVisibility(View.VISIBLE);
            activityUpdateRouteBinding.placesLayout.setVisibility(View.GONE);
            activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            hideKeyboard();
        } else if (updateRouteViewModel.isSDAddressFilled(source, destination) && sourceAddressLatLng != null && destinationAddressLatLng != null) {
            activityUpdateRouteBinding.confirm.setVisibility(View.VISIBLE);
            activityUpdateRouteBinding.appreciateLayout.setVisibility(View.GONE);
            hideKeyboard();
            activityUpdateRouteBinding.placesLayout.setVisibility(View.GONE);
            activityUpdateRouteBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            activityUpdateRouteBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        } else {
            activityUpdateRouteBinding.confirm.setVisibility(View.GONE);
            activityUpdateRouteBinding.appreciateLayout.setVisibility(View.GONE);
            activityUpdateRouteBinding.placesLayout.setVisibility(View.VISIBLE);
        }
    }


}
