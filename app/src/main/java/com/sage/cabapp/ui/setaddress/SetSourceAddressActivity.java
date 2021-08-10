package com.sage.cabapp.ui.setaddress;

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
import com.sage.cabapp.databinding.ActivitySetAddressBinding;
import com.sage.cabapp.ui.addhomeplace.AddHomePlaceActivity;
import com.sage.cabapp.ui.addworkplace.AddWorkPlaceActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.locationonmap.LocationOnMapActivity;
import com.sage.cabapp.ui.requestforcar.RequestForCarActivity;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;
import com.sage.cabapp.ui.setaddress.adapter.DestinationPlacesAutoCompleteAdapter;
import com.sage.cabapp.ui.setaddress.adapter.SourcePlacesAutoCompleteAdapter;
import com.sage.cabapp.ui.setaddress.model.PlaceAutocomplete;
import com.sage.cabapp.utils.Constant;

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
public class SetSourceAddressActivity extends BaseActivity<ActivitySetAddressBinding, SetAddressViewModel> implements SetAddressNavigator, SourceAddressClickCallback, DestinationAddressClickCallback {

    @Inject
    ViewModelProviderFactory factory;
    private SetAddressViewModel setAddressViewModel;
    private ActivitySetAddressBinding activitySetAddressBinding;
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

    private boolean homeFound = false;
    private boolean workFound = false;
    SkeletonScreen sourceSkeletonScreen = null;
    SkeletonScreen destinationSkeletonScreen = null;

    @Override
    public int getBindingVariable() {
        return BR.setAddressViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_address;
    }

    @Override
    public SetAddressViewModel getViewModel() {
        setAddressViewModel = ViewModelProviders.of(this, factory).get(SetAddressViewModel.class);
        return setAddressViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetAddressBinding = getViewDataBinding();
        setAddressViewModel.setNavigator(this);

        Places.initialize(this, getResources().getString(R.string.places_api_key));
        placesClient = Places.createClient(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ADDRESS")) {
            activitySetAddressBinding.etSourceaddress.setText(intent.getStringExtra("ADDRESS"));
            sourceAddressLatLng = intent.getParcelableExtra("LAT_LNG");

            if (intent.hasExtra("DEST_LAT_LNG")){
                destinationAddressLatLng = intent.getParcelableExtra("DEST_LAT_LNG");
                activitySetAddressBinding.etDestinationaddress.setText(intent.getStringExtra("DEST_ADDRESS"));

                checkFilledAddress();
            }
        }

        activitySetAddressBinding.sourcePlacesRecyclerView.setNestedScrollingEnabled(false);
        activitySetAddressBinding.destinationPlacesRecyclerView.setNestedScrollingEnabled(false);
        sourceAddressView();
        destinationAddressView();

        activitySetAddressBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

        addTextWatcherSource();
        addTextWatcherDestination();
        addTextWatcherDestinationSecond();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherSource() {

        activitySetAddressBinding.etSourceaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activitySetAddressBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activitySetAddressBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activitySetAddressBinding.etSourceaddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activitySetAddressBinding.etSourceaddress.getRight() - activitySetAddressBinding.etSourceaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activitySetAddressBinding.etSourceaddress.setText("");
                            checkFilledAddress();
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activitySetAddressBinding.etSourceaddress.postDelayed(() -> activitySetAddressBinding.etSourceaddress.setSelection(Objects.requireNonNull(activitySetAddressBinding.etSourceaddress.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                activitySetAddressBinding.etSourceaddress.setCursorVisible(true);

                activitySetAddressBinding.etDestinationaddress.setSelection(0);
                activitySetAddressBinding.etDestinationaddress.setCursorVisible(false);

                if (addStop) {
                    activitySetAddressBinding.etDestinationaddresssecond.setSelection(0);
                    activitySetAddressBinding.etDestinationaddresssecond.setCursorVisible(false);
                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherDestination() {

        activitySetAddressBinding.etDestinationaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activitySetAddressBinding.etDestinationaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activitySetAddressBinding.etDestinationaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activitySetAddressBinding.etDestinationaddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activitySetAddressBinding.etDestinationaddress.getRight() - activitySetAddressBinding.etDestinationaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activitySetAddressBinding.etDestinationaddress.setText("");
                            checkFilledAddress();
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activitySetAddressBinding.etDestinationaddress.postDelayed(() -> activitySetAddressBinding.etDestinationaddress.setSelection(Objects.requireNonNull(activitySetAddressBinding.etDestinationaddress.getText()).toString().length()), 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activitySetAddressBinding.etDestinationaddress.setCursorVisible(true);

                activitySetAddressBinding.etSourceaddress.setSelection(0);
                activitySetAddressBinding.etSourceaddress.setCursorVisible(false);

                if (addStop) {
                    activitySetAddressBinding.etDestinationaddresssecond.setSelection(0);
                    activitySetAddressBinding.etDestinationaddresssecond.setCursorVisible(false);
                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherDestinationSecond() {

        activitySetAddressBinding.etDestinationaddresssecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activitySetAddressBinding.etDestinationaddresssecond.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activitySetAddressBinding.etDestinationaddresssecond.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activitySetAddressBinding.etDestinationaddresssecond.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activitySetAddressBinding.etDestinationaddresssecond.getRight() - activitySetAddressBinding.etDestinationaddresssecond.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activitySetAddressBinding.etDestinationaddresssecond.setText("");
                            checkFilledAddress();
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activitySetAddressBinding.etDestinationaddresssecond.postDelayed(() -> activitySetAddressBinding.etDestinationaddresssecond.setSelection(Objects.requireNonNull(activitySetAddressBinding.etDestinationaddresssecond.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                activitySetAddressBinding.etDestinationaddresssecond.setCursorVisible(true);

                activitySetAddressBinding.etSourceaddress.setSelection(0);
                activitySetAddressBinding.etSourceaddress.setCursorVisible(false);

                activitySetAddressBinding.etDestinationaddress.setSelection(0);
                activitySetAddressBinding.etDestinationaddress.setCursorVisible(false);

                return false;
            }
        });
    }

    private void sourceAddressView() {

        activitySetAddressBinding.etSourceaddress.addTextChangedListener(sourcefilterTextWatcher);
        sourcePlacesAutoCompleteAdapter = new SourcePlacesAutoCompleteAdapter(this);
        activitySetAddressBinding.sourcePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sourcePlacesAutoCompleteAdapter.setSourceAddressClickCallback(this);
        activitySetAddressBinding.sourcePlacesRecyclerView.setAdapter(sourcePlacesAutoCompleteAdapter);
        activitySetAddressBinding.sourcePlacesRecyclerView.getRecycledViewPool().clear();
        sourcePlacesAutoCompleteAdapter.notifyDataSetChanged();
    }

    private void destinationAddressView() {

        activitySetAddressBinding.etDestinationaddress.addTextChangedListener(destinationfilterTextWatcher);
        activitySetAddressBinding.etDestinationaddresssecond.addTextChangedListener(seconddestinationfilterTextWatcher);
        destinationPlacesAutoCompleteAdapter = new DestinationPlacesAutoCompleteAdapter(this);
        activitySetAddressBinding.destinationPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        destinationPlacesAutoCompleteAdapter.setDestinationAddressClickCallback(this);
        activitySetAddressBinding.destinationPlacesRecyclerView.setAdapter(destinationPlacesAutoCompleteAdapter);
        activitySetAddressBinding.destinationPlacesRecyclerView.getRecycledViewPool().clear();
        destinationPlacesAutoCompleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void addMoreStopClick() {

        if (activitySetAddressBinding.addStopLayout.getVisibility() == View.GONE) {

            addStop = true;
            activitySetAddressBinding.addStopLayout.setVisibility(View.VISIBLE);
            activitySetAddressBinding.addStopAddressLine.setVisibility(View.VISIBLE);
            activitySetAddressBinding.addStopView.setVisibility(View.VISIBLE);
            activitySetAddressBinding.addClick.setBackgroundResource(R.drawable.ic_remove_gray);
        } else {

            addStop = false;
            activitySetAddressBinding.addStopLayout.setVisibility(View.GONE);
            activitySetAddressBinding.addStopAddressLine.setVisibility(View.GONE);
            activitySetAddressBinding.addStopView.setVisibility(View.GONE);
            activitySetAddressBinding.addClick.setBackgroundResource(R.drawable.ic_add_grey);
        }

        activitySetAddressBinding.etDestinationaddresssecond.setText("");

        checkFilledAddress();
    }

    @Override
    public void cancelStopClick() {

        addStop = false;
        activitySetAddressBinding.addStopLayout.setVisibility(View.GONE);
        activitySetAddressBinding.etDestinationaddresssecond.setText("");
        activitySetAddressBinding.addStopAddressLine.setVisibility(View.GONE);
        activitySetAddressBinding.addStopView.setVisibility(View.GONE);
        activitySetAddressBinding.addClick.setBackgroundResource(R.drawable.ic_add_grey);

        checkFilledAddress();
    }

    @Override
    public void clickHome() {

        if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
        }

        if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        }

        String address = "";
        if (homeFound) {
            address = activitySetAddressBinding.actualHomeAddress.getText().toString().trim();
            activitySetAddressBinding.etDestinationaddress.setText(address);
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

        if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
        }

        if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        }

        String address = "";
        if (workFound) {
            address = activitySetAddressBinding.actualWorkAddress.getText().toString().trim();
            activitySetAddressBinding.etDestinationaddress.setText(address);
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
    public void mapClick() {

        if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
        }

        if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
            activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        }

        Intent intent = new Intent(this, LocationOnMapActivity.class);
        intent.putExtra("SOURCELATLNG", sourceAddressLatLng);
        startActivityForResult(intent, 1111);
        slideRightToLeft();
    }


    @Override
    public void confirmSetAddress() {

        activitySetAddressBinding.appreciateLayout.setVisibility(View.GONE);

        String source = Objects.requireNonNull(activitySetAddressBinding.etSourceaddress.getText()).toString().trim();
        String destination = Objects.requireNonNull(activitySetAddressBinding.etDestinationaddress.getText()).toString().trim();
        String addstop = Objects.requireNonNull(activitySetAddressBinding.etDestinationaddresssecond.getText()).toString().trim();

        if (TextUtils.isEmpty(addstop)) {
            addStopAddressLatLng = null;
        }

        Intent intent = new Intent(this, RequestForCarActivity.class);
        intent.putExtra("SOURCE_ADD", source);
        intent.putExtra("SOURCE_LATLNG", sourceAddressLatLng);
        intent.putExtra("DEST_ADD", destination);
        intent.putExtra("DEST_LATLNG", destinationAddressLatLng);
        intent.putExtra("ADDSTOP_ADD", addstop);
        intent.putExtra("ADDSTOP_LATLNG", addStopAddressLatLng);
        startActivity(intent);
        slideRightToLeft();
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
                            activitySetAddressBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_green);

                            activitySetAddressBinding.homeAddress.setText("Home");
                            activitySetAddressBinding.actualHomeAddress.setText(getAllPlacesData.getAddress());
                            activitySetAddressBinding.actualHomeAddress.setVisibility(View.VISIBLE);

                            destinationHome = new LatLng(Double.parseDouble(getAllPlacesData.getLat()), Double.parseDouble(getAllPlacesData.getLng()));
                            homeFound = true;
                        } else if (getAllPlacesData.getType().equalsIgnoreCase("work")) {
                            activitySetAddressBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_green);

                            activitySetAddressBinding.workAddress.setText("Work");
                            activitySetAddressBinding.actualWorkAddress.setText(getAllPlacesData.getAddress());
                            activitySetAddressBinding.actualWorkAddress.setVisibility(View.VISIBLE);
                            destinationWork = new LatLng(Double.parseDouble(getAllPlacesData.getLat()), Double.parseDouble(getAllPlacesData.getLng()));

                            workFound = true;
                        }
                    }

                    if (!homeFound) {
                        activitySetAddressBinding.homeAddress.setText("Add home");
                        activitySetAddressBinding.actualHomeAddress.setVisibility(View.GONE);
                        activitySetAddressBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                    } else if (!workFound) {
                        activitySetAddressBinding.workAddress.setText("Add work");
                        activitySetAddressBinding.actualWorkAddress.setVisibility(View.GONE);
                        activitySetAddressBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
                    }

                } else {
                    activitySetAddressBinding.homeAddress.setText("Add home");
                    activitySetAddressBinding.actualHomeAddress.setVisibility(View.GONE);

                    activitySetAddressBinding.workAddress.setText("Add work");
                    activitySetAddressBinding.actualWorkAddress.setVisibility(View.GONE);

                    activitySetAddressBinding.addHomeImage.setBackgroundResource(R.drawable.ic_home_grey);
                    activitySetAddressBinding.addWorkImage.setBackgroundResource(R.drawable.ic_work_grey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.GONE && activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.GONE) {
            activitySetAddressBinding.placesLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            setAddressViewModel.getAllPlacesWS(this);
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
                activitySetAddressBinding.etSourceaddress.setText(address);
                activitySetAddressBinding.etSourceaddress.setSelection(0);
                activitySetAddressBinding.etSourceaddress.setCursorVisible(false);

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
                if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.GONE) {
                    activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                addSourceSkeleton();

                if (activitySetAddressBinding.placesLayout.getVisibility() == View.VISIBLE) {
                    activitySetAddressBinding.placesLayout.setVisibility(View.GONE);
                }
            } else {
                if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
                }

                if (activitySetAddressBinding.placesLayout.getVisibility() == View.GONE) {
                    activitySetAddressBinding.placesLayout.setVisibility(View.VISIBLE);
                }
            }

            if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            }


        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void addSourceSkeleton() {

        sourceSkeletonScreen = Skeleton.bind(activitySetAddressBinding.sourcePlacesRecyclerView)
                .adapter(sourcePlacesAutoCompleteAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1500)
                .count(10)
                .load(R.layout.shimmer_set_address)
                .show();

        activitySetAddressBinding.sourcePlacesRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sourceSkeletonScreen.hide();
            }
        }, 1500);
    }

    private void destinationSkeleton() {

        destinationSkeletonScreen = Skeleton.bind(activitySetAddressBinding.destinationPlacesRecyclerView)
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
                if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.GONE) {
                    activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                destinationSkeleton();

                if (activitySetAddressBinding.placesLayout.getVisibility() == View.VISIBLE) {
                    activitySetAddressBinding.placesLayout.setVisibility(View.GONE);
                }
            } else {
                if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
                }

                if (activitySetAddressBinding.placesLayout.getVisibility() == View.GONE) {
                    activitySetAddressBinding.placesLayout.setVisibility(View.VISIBLE);
                }
            }

            if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
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
                if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.GONE) {
                    activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                destinationSkeleton();
            } else {
                if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
                }
            }

            if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
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

            activitySetAddressBinding.etSourceaddress.setText(place1.getName());
            activitySetAddressBinding.etSourceaddress.setSelection(Objects.requireNonNull(activitySetAddressBinding.etSourceaddress.getText()).length());


            if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activitySetAddressBinding.placesLayout.getVisibility() == View.GONE) {
                activitySetAddressBinding.placesLayout.setVisibility(View.VISIBLE);
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

                activitySetAddressBinding.etDestinationaddresssecond.setText(place1.getName());
                activitySetAddressBinding.etDestinationaddresssecond.setSelection(0);
                activitySetAddressBinding.etDestinationaddresssecond.setCursorVisible(false);

            } else {

                destinationAddressLatLng = place1.getLatLng();

                activitySetAddressBinding.etDestinationaddress.setText(place1.getName());
                activitySetAddressBinding.etDestinationaddress.setSelection(0);
                activitySetAddressBinding.etDestinationaddress.setCursorVisible(false);
            }

            if (activitySetAddressBinding.destinationPlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activitySetAddressBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }

            if (activitySetAddressBinding.placesLayout.getVisibility() == View.GONE) {
                activitySetAddressBinding.placesLayout.setVisibility(View.VISIBLE);
            }

            checkFilledAddress();

        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                Toast.makeText(getApplicationContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFilledAddress() {
        String source = Objects.requireNonNull(activitySetAddressBinding.etSourceaddress.getText()).toString().trim();
        String destination = Objects.requireNonNull(activitySetAddressBinding.etDestinationaddress.getText()).toString().trim();
        String addstop = Objects.requireNonNull(activitySetAddressBinding.etDestinationaddresssecond.getText()).toString().trim();

        if (setAddressViewModel.isAllAddressFilled(source, destination, addstop) && sourceAddressLatLng != null && destinationAddressLatLng != null && addStopAddressLatLng != null) {
            activitySetAddressBinding.confirm.setVisibility(View.VISIBLE);
            activitySetAddressBinding.appreciateLayout.setVisibility(View.VISIBLE);
            activitySetAddressBinding.placesLayout.setVisibility(View.GONE);
            activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
            hideKeyboard();
        } else if (setAddressViewModel.isSDAddressFilled(source, destination) && sourceAddressLatLng != null && destinationAddressLatLng != null) {
            activitySetAddressBinding.confirm.setVisibility(View.VISIBLE);
            activitySetAddressBinding.appreciateLayout.setVisibility(View.GONE);
            hideKeyboard();
            activitySetAddressBinding.placesLayout.setVisibility(View.GONE);
            activitySetAddressBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            activitySetAddressBinding.destinationPlacesRecyclerView.setVisibility(View.GONE);
        } else {
            activitySetAddressBinding.confirm.setVisibility(View.GONE);
            activitySetAddressBinding.appreciateLayout.setVisibility(View.GONE);
            activitySetAddressBinding.placesLayout.setVisibility(View.VISIBLE);
        }
    }


}
