package com.sage.cabapp.ui.setaddressconfirmpickup;

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
import com.sage.cabapp.databinding.ActivitySetAddressPickupBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.setaddress.model.PlaceAutocomplete;
import com.sage.cabapp.ui.setaddressconfirmpickup.adapter.SourcePlacesPickupAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 17-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SetAddressPickupActivity extends BaseActivity<ActivitySetAddressPickupBinding, SetAddressPickupViewModel> implements SetAddressPickupNavigator, ConfirmAddressClickCallback {

    @Inject
    ViewModelProviderFactory factory;
    SetAddressPickupViewModel setAddressPickupViewModel;
    ActivitySetAddressPickupBinding activitySetAddressPickupBinding;
    private PlacesClient placesClient;
    SourcePlacesPickupAdapter sourcePlacesPickupAdapter;
    private ArrayList<PlaceAutocomplete> sourcemResultList = new ArrayList<>();
    private LatLng sourceAddressLatLng = null;
    SkeletonScreen sourceSkeletonScreen = null;

    @Override
    public int getBindingVariable() {
        return BR.setAddressPickupViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_address_pickup;
    }

    @Override
    public SetAddressPickupViewModel getViewModel() {
        setAddressPickupViewModel = ViewModelProviders.of(this,factory).get(SetAddressPickupViewModel.class);
        return setAddressPickupViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySetAddressPickupBinding = getViewDataBinding();
        setAddressPickupViewModel.setNavigator(this);

        Places.initialize(this, getResources().getString(R.string.places_api_key));
        placesClient = Places.createClient(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ADDRESS")) {
            activitySetAddressPickupBinding.etSourceaddress.setText(intent.getStringExtra("ADDRESS"));
            activitySetAddressPickupBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
        }

        sourceAddressView();

        addTextWatcherSource();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTextWatcherSource() {

        activitySetAddressPickupBinding.etSourceaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activitySetAddressPickupBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activitySetAddressPickupBinding.etSourceaddress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activitySetAddressPickupBinding.etSourceaddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activitySetAddressPickupBinding.etSourceaddress.getRight() - activitySetAddressPickupBinding.etSourceaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activitySetAddressPickupBinding.etSourceaddress.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    activitySetAddressPickupBinding.etSourceaddress.postDelayed(() -> activitySetAddressPickupBinding.etSourceaddress.setSelection(Objects.requireNonNull(activitySetAddressPickupBinding.etSourceaddress.getText()).toString().length()),100);

                }catch (Exception e){
                    e.printStackTrace();
                }
                activitySetAddressPickupBinding.etSourceaddress.setCursorVisible(true);

                return false;
            }
        });
    }

    private void sourceAddressView() {

        activitySetAddressPickupBinding.etSourceaddress.addTextChangedListener(sourcefilterTextWatcher);
        sourcePlacesPickupAdapter = new SourcePlacesPickupAdapter(this);
        activitySetAddressPickupBinding.sourcePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sourcePlacesPickupAdapter.setSourceAddressClickCallback(this);
        activitySetAddressPickupBinding.sourcePlacesRecyclerView.setAdapter(sourcePlacesPickupAdapter);
        activitySetAddressPickupBinding.sourcePlacesRecyclerView.getRecycledViewPool().clear();
        sourcePlacesPickupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void sourceAddressClick(PlaceAutocomplete place) {
        String placeId = String.valueOf(place.placeId);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place1 = response.getPlace();

            sourceAddressLatLng = place1.getLatLng();

            activitySetAddressPickupBinding.etSourceaddress.setText(place1.getName());
            activitySetAddressPickupBinding.etSourceaddress.setSelection(Objects.requireNonNull(activitySetAddressPickupBinding.etSourceaddress.getText()).length());

            if (activitySetAddressPickupBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                activitySetAddressPickupBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("address", place1.getName());
            returnIntent.putExtra("latlng", sourceAddressLatLng);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            slideLeftToRight();

        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                Toast.makeText(getApplicationContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextWatcher sourcefilterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                getFilterSource().filter(s.toString());
                if (activitySetAddressPickupBinding.sourcePlacesRecyclerView.getVisibility() == View.GONE) {
                    activitySetAddressPickupBinding.sourcePlacesRecyclerView.setVisibility(View.VISIBLE);
                }

                addSourceSkeleton();
            } else {
                if (activitySetAddressPickupBinding.sourcePlacesRecyclerView.getVisibility() == View.VISIBLE) {
                    activitySetAddressPickupBinding.sourcePlacesRecyclerView.setVisibility(View.GONE);
                }
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void addSourceSkeleton() {

        sourceSkeletonScreen = Skeleton.bind(activitySetAddressPickupBinding.sourcePlacesRecyclerView)
                .adapter(sourcePlacesPickupAdapter)
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
                        sourcePlacesPickupAdapter.addAll(sourcemResultList);
                    }

                    sourceSkeletonScreen.hide();
                    sourcePlacesPickupAdapter.notifyDataSetChanged();
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
}
