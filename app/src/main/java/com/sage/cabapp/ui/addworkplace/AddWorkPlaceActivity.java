package com.sage.cabapp.ui.addworkplace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
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
import com.sage.cabapp.databinding.ActivityAddWorkPlaceBinding;
import com.sage.cabapp.ui.addhomeplace.adapter.AddHomePlacesAutoCompleteAdapter;
import com.sage.cabapp.ui.addhomeplace.fragment.DeletePlacesFragment;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.locationonmap.LocationOnMapActivity;
import com.sage.cabapp.ui.savedplaces.model.AddPlaceResponse;
import com.sage.cabapp.ui.setaddress.SourceAddressClickCallback;
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
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddWorkPlaceActivity extends BaseActivity<ActivityAddWorkPlaceBinding, AddWorkPlaceViewModel> implements AddWorkPlaceNavigator, SourceAddressClickCallback {

    @Inject
    ViewModelProviderFactory factory;
    AddWorkPlaceViewModel addWorkPlaceViewModel;
    ActivityAddWorkPlaceBinding activityAddWorkPlaceBinding;
    AddHomePlacesAutoCompleteAdapter addHomePlacesAutoCompleteAdapter;
    private PlacesClient placesClient;
    private ArrayList<PlaceAutocomplete> sourcemResultList = new ArrayList<>();
    private LatLng sourceAddressLatLng = null;
    private boolean workFound = false;
    private String address = "";
    SkeletonScreen sourceSkeletonScreen = null;
    boolean isFromMap = false;

    @Override
    public int getBindingVariable() {
        return BR.addWorkViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_work_place;
    }

    @Override
    public AddWorkPlaceViewModel getViewModel() {
        addWorkPlaceViewModel = ViewModelProviders.of(this, factory).get(AddWorkPlaceViewModel.class);
        return addWorkPlaceViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddWorkPlaceBinding = getViewDataBinding();
        addWorkPlaceViewModel.setNavigator(this);

        Places.initialize(this, getResources().getString(R.string.places_api_key));
        placesClient = Places.createClient(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("IS_ADDED")) {
            workFound = intent.getBooleanExtra("IS_ADDED", false);
            address = intent.getStringExtra("ADDRESS");
            sourceAddressLatLng = intent.getParcelableExtra("workLatLng");
        }

        if (workFound) {
            activityAddWorkPlaceBinding.workText.setText("Work");
            activityAddWorkPlaceBinding.etHomeaddress.setText(address);
            activityAddWorkPlaceBinding.deleteWork.setVisibility(View.VISIBLE);
            activityAddWorkPlaceBinding.map.setVisibility(View.GONE);

            checkFilledAddress();

            activityAddWorkPlaceBinding.etHomeaddress.setCursorVisible(false);
            activityAddWorkPlaceBinding.etHomeaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);


        } else {
            activityAddWorkPlaceBinding.workText.setText("Add work");
            activityAddWorkPlaceBinding.deleteWork.setVisibility(View.GONE);
            activityAddWorkPlaceBinding.map.setVisibility(View.VISIBLE);
        }

        sourceAddressView();


        activityAddWorkPlaceBinding.etHomeaddress.setOnTouchListener((v, event) -> {

            try {
                activityAddWorkPlaceBinding.etHomeaddress.postDelayed(() -> activityAddWorkPlaceBinding.etHomeaddress.setSelection(Objects.requireNonNull(activityAddWorkPlaceBinding.etHomeaddress.getText()).toString().length()), 100);

            } catch (Exception e) {
                e.printStackTrace();
            }

            activityAddWorkPlaceBinding.etHomeaddress.setCursorVisible(true);

            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {

                try {
                    if (event.getRawX() >= (activityAddWorkPlaceBinding.etHomeaddress.getRight() - activityAddWorkPlaceBinding.etHomeaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        activityAddWorkPlaceBinding.etHomeaddress.setText("");
                        activityAddWorkPlaceBinding.confirmHomeAddress.setVisibility(View.GONE);
                        activityAddWorkPlaceBinding.noAddressFound.setVisibility(View.GONE);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
            return false;
        });


        activityAddWorkPlaceBinding.etHomeaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAddWorkPlaceBinding.etHomeaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAddWorkPlaceBinding.etHomeaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
    public void submitHomeAddress() {
        String address = Objects.requireNonNull(activityAddWorkPlaceBinding.etHomeaddress.getText()).toString().trim();

        if (!address.isEmpty() && sourceAddressLatLng != null) {
            if (isNetworkConnected()) {
                showLoading("");
                addWorkPlaceViewModel.addPlaceWS(this, address, String.valueOf(sourceAddressLatLng.latitude), String.valueOf(sourceAddressLatLng.longitude));
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        } else {
            vibrate();
            Constant.showErrorToast("Please enter address", this);
        }
    }

    @Override
    public void fromMap() {
        if (activityAddWorkPlaceBinding.homeaddressRecyclerView.getVisibility() == View.VISIBLE) {
            activityAddWorkPlaceBinding.homeaddressRecyclerView.setVisibility(View.GONE);
        }

        Intent intent = new Intent(this, LocationOnMapActivity.class);
        intent.putExtra("SOURCELATLNG", sourceAddressLatLng);
        startActivityForResult(intent, 1111);
        slideRightToLeft();
    }

    @Override
    public void delete() {
        Bundle bundle = new Bundle();
        bundle.putString("placeType", "work");

        DeletePlacesFragment fragment = DeletePlacesFragment.newInstance();
        fragment.setCancelable(false);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "delete");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            if (resultCode == Activity.RESULT_OK) {

                isFromMap = true;

                String address = Objects.requireNonNull(data).getStringExtra("address");
                sourceAddressLatLng = Objects.requireNonNull(data).getParcelableExtra("tapLocation");
                activityAddWorkPlaceBinding.etHomeaddress.setText(address);

                activityAddWorkPlaceBinding.etHomeaddress.setCursorVisible(false);
                activityAddWorkPlaceBinding.etHomeaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

                checkFilledAddress();

            }
        }
    }


    @Override
    public void successAPI(AddPlaceResponse addPlaceResponse) {

        hideLoading();

        try {
            if (addPlaceResponse.getStatus() != null && addPlaceResponse.getStatus().equalsIgnoreCase("true")) {

                finish();
                slideLeftToRight();
            } else {
                vibrate();
                Constant.showErrorToast(addPlaceResponse.getMessage(), this);
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

    private void sourceAddressView() {

        activityAddWorkPlaceBinding.etHomeaddress.addTextChangedListener(sourcefilterTextWatcher);
        addHomePlacesAutoCompleteAdapter = new AddHomePlacesAutoCompleteAdapter(this);
        activityAddWorkPlaceBinding.homeaddressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addHomePlacesAutoCompleteAdapter.setSourceAddressClickCallback(this);
        activityAddWorkPlaceBinding.homeaddressRecyclerView.setAdapter(addHomePlacesAutoCompleteAdapter);
        activityAddWorkPlaceBinding.homeaddressRecyclerView.getRecycledViewPool().clear();
        addHomePlacesAutoCompleteAdapter.notifyDataSetChanged();
    }

    private TextWatcher sourcefilterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                if (!isFromMap) {
                    getFilterSource().filter(s.toString());
                    if (activityAddWorkPlaceBinding.homeaddressRecyclerView.getVisibility() == View.GONE) {
                        activityAddWorkPlaceBinding.homeaddressRecyclerView.setVisibility(View.VISIBLE);
                    }

                    addSourceSkeleton();

                    if (activityAddWorkPlaceBinding.confirmHomeAddress.getVisibility() == View.VISIBLE) {
                        activityAddWorkPlaceBinding.confirmHomeAddress.setVisibility(View.GONE);
                    }
                } else {
                    isFromMap = false;
                }

            } else {
                if (activityAddWorkPlaceBinding.homeaddressRecyclerView.getVisibility() == View.VISIBLE) {
                    activityAddWorkPlaceBinding.homeaddressRecyclerView.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void addSourceSkeleton() {

        sourceSkeletonScreen = Skeleton.bind(activityAddWorkPlaceBinding.homeaddressRecyclerView)
                .adapter(addHomePlacesAutoCompleteAdapter)
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
                        addHomePlacesAutoCompleteAdapter.addAll(sourcemResultList);
                    }

                    sourceSkeletonScreen.hide();
                    addHomePlacesAutoCompleteAdapter.notifyDataSetChanged();
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

    @Override
    public void sourceAddressClick(PlaceAutocomplete place) {
        String placeId = String.valueOf(place.placeId);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place1 = response.getPlace();

            sourceAddressLatLng = place1.getLatLng();

            activityAddWorkPlaceBinding.etHomeaddress.setText(place1.getName());
            activityAddWorkPlaceBinding.etHomeaddress.setSelection(Objects.requireNonNull(activityAddWorkPlaceBinding.etHomeaddress.getText()).length());

            if (activityAddWorkPlaceBinding.homeaddressRecyclerView.getVisibility() == View.VISIBLE) {
                activityAddWorkPlaceBinding.homeaddressRecyclerView.setVisibility(View.GONE);
            }

            hideKeyboard();
            checkFilledAddress();

        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                Toast.makeText(getApplicationContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFilledAddress() {
        String source = Objects.requireNonNull(activityAddWorkPlaceBinding.etHomeaddress.getText()).toString().trim();

        if (addWorkPlaceViewModel.isAllAddressFilled(source)) {
            activityAddWorkPlaceBinding.confirmHomeAddress.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else {
            activityAddWorkPlaceBinding.confirmHomeAddress.setVisibility(View.GONE);
        }
    }

}
