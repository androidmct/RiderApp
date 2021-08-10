package com.sage.cabapp.ui.tripinquirypage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.androidnetworking.error.ANError;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.ramotion.directselect.DSListView;
import com.sage.cabapp.BR;
import com.sage.cabapp.BuildConfig;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityTripInquiryBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.triphistory.model.TripHistoryResponseData;
import com.sage.cabapp.ui.tripinquirypage.adapter.AdvancedProblemAdapter;
import com.sage.cabapp.ui.tripinquirypage.adapter.InquiryImagesAdapter;
import com.sage.cabapp.ui.tripinquirypage.fragment.InquirySubmittedFragment;
import com.sage.cabapp.ui.tripinquirypage.model.AdvancedProblemPOJO;
import com.sage.cabapp.ui.tripinquirypage.model.TripInquiryResponse;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripInquiryActivity extends BaseActivity<ActivityTripInquiryBinding, TripInquiryViewModel> implements TripInquiryNavigator, OnMapReadyCallback, InquiryImageDelete {

    @Inject
    ViewModelProviderFactory factory;
    TripInquiryViewModel tripInquiryViewModel;
    ActivityTripInquiryBinding activityTripInquiryBinding;
    TripHistoryResponseData tripHistoryResponseData = null;
    private LatLng sourceAddressLatLng = null;
    private LatLng destinationAddressLatLng = null;
    private LatLng addStopAddressLatLng = null;
    private GoogleMap mGoogleMap;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean isGallery = true;
    private InquiryImagesAdapter inquiryImagesAdapter = null;
    List<File> imagesList = new ArrayList<>();

    @Override
    public int getBindingVariable() {
        return BR.tripInquiryViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trip_inquiry;
    }

    @Override
    public TripInquiryViewModel getViewModel() {
        tripInquiryViewModel = ViewModelProviders.of(this, factory).get(TripInquiryViewModel.class);
        return tripInquiryViewModel;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityTripInquiryBinding = getViewDataBinding();
        tripInquiryViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TRIP_DETAIL")) {
            tripHistoryResponseData = (TripHistoryResponseData) intent.getSerializableExtra("TRIP_DETAIL");
            sourceAddressLatLng = (LatLng) Objects.requireNonNull(intent.getExtras()).get("sourceAddressLatLng");
            destinationAddressLatLng = (LatLng) intent.getExtras().get("destinationAddressLatLng");
            if (intent.hasExtra("addStopAddressLatLng")) {
                addStopAddressLatLng = (LatLng) intent.getExtras().get("addStopAddressLatLng");
            }
        }

        if (tripHistoryResponseData != null) {
            activityTripInquiryBinding.tripDate.setText(Constant.convertDate(tripHistoryResponseData.getCreatedAt()));
            activityTripInquiryBinding.tripFare.setText(String.format("$%s", tripHistoryResponseData.getRideFare()));

            activityTripInquiryBinding.rating.setRating(tripHistoryResponseData.getTripRating());

            if (sourceAddressLatLng != null && destinationAddressLatLng != null) {

                if (addStopAddressLatLng != null) {
                    if (isNetworkConnected()) {
                        addRouteWaypoints();
                    }
                } else {
                    if (isNetworkConnected()) {
                        addRoute();
                    }
                }

            }

        }

        initializeMap();

        setProblemData();

        addFile();
    }

    DSListView<AdvancedProblemPOJO> advancedProblemPOJODSListView;

    private void setProblemData() {

        List<AdvancedProblemPOJO> exampleDataSet = AdvancedProblemPOJO.getExampleDataset();

        // Create adapter with our dataset
        ArrayAdapter<AdvancedProblemPOJO> adapter = new AdvancedProblemAdapter(this, R.layout.advanced_problem_list_item, exampleDataSet);

        // Set adapter to our DSListView
        advancedProblemPOJODSListView = activityTripInquiryBinding.dsProblemList;

        advancedProblemPOJODSListView.setAdapter(adapter);

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
    public void submitInquiry() {

        if (isNetworkConnected()) {

            String mobile = SharedData.getString(this, Constant.PHONE_NUMBER);
            String email = SharedData.getString(this, Constant.EMAIL);
            String problemType = advancedProblemPOJODSListView.getSelectedItem().getTitle();
            String problemDetail = Objects.requireNonNull(activityTripInquiryBinding.commentsEt.getText()).toString().trim();

            showLoading("");

            if (imagesList != null && imagesList.size() > 0) {
                tripInquiryViewModel.inquirySubmissionWS(this, encodeBase64(mobile), encodeBase64(email), tripHistoryResponseData.getRequestId(), problemType, problemDetail, imagesList);
            } else {
                tripInquiryViewModel.inquirySubmissionWS(this, encodeBase64(mobile), encodeBase64(email), tripHistoryResponseData.getRequestId(), problemType, problemDetail, null);
            }
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void addImagesClick() {

        if (imagesList != null && imagesList.size() == 6) {
            Constant.showErrorToast("Maximum 6 images are allow to submit!", this);
        } else {
            openDialog();
        }
    }

    @Override
    public void successAPI(TripInquiryResponse response) {

        hideLoading();

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                showInquirySubmitted();

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
    public boolean dispatchTouchEvent(MotionEvent ev) {

        hideKeyboard();

        if (activityTripInquiryBinding.dsProblemList.getVisibility() == View.VISIBLE) {
            ((NestedScrollView) (findViewById(R.id.scrollview))).requestDisallowInterceptTouchEvent(true);
        }

        if (activityTripInquiryBinding.dsProblemList.getVisibility() != View.VISIBLE) {
            ((NestedScrollView) (findViewById(R.id.scrollview))).requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);

    }

    InquirySubmittedFragment inquirySubmittedFragment = null;

    void showInquirySubmitted() {

        inquirySubmittedFragment = InquirySubmittedFragment.newInstance();
        inquirySubmittedFragment.setCancelable(true);
        inquirySubmittedFragment.show(getSupportFragmentManager(), "inquirySubmitted");

        new CountDownTimer(2000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (inquirySubmittedFragment != null && inquirySubmittedFragment.isVisible()) {
                    inquirySubmittedFragment.dismiss();
                    inquirySubmittedFragment = null;
                    this.cancel();
                }

                finish();
                slideLeftToRight();
            }
        }.start();
    }

    private void addRouteWaypoints() {
        GoogleDirection.withServerKey("AIzaSyAXTjpjJS6iEyHr_-LV1ZargDtGPo9B6yI")
                .from(sourceAddressLatLng)
                .and(addStopAddressLatLng)
                .to(destinationAddressLatLng)

                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            // Do something
                            onDirectionWayPoints11(direction);
                        } else {
                            // Do something
                            Constant.showErrorToast(direction.getErrorMessage(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        Constant.showErrorToast(t.getMessage(), getApplicationContext());
                    }
                });
    }

    private void addRoute() {
        GoogleDirection.withServerKey("AIzaSyAXTjpjJS6iEyHr_-LV1ZargDtGPo9B6yI")
                .from(sourceAddressLatLng)
                .to(destinationAddressLatLng)

                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            // Do something
                            onDirection11(direction);
                        } else {
                            // Do something
                            Constant.showErrorToast(direction.getErrorMessage(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        Constant.showErrorToast(t.getMessage(), getApplicationContext());
                    }
                });
    }

    private void onDirection(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        Route route = direction.getRouteList().get(0);
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        mGoogleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, ContextCompat.getColor(this, R.color.colorPrimary)));

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirection11(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        if (gradientpoly != null && gradientpoly.size() > 0) {
            gradientpoly.clear();
        }

        Route route = direction.getRouteList().get(0);
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();

        LatLng source = directionPositionList.get(0);
        LatLng dest = directionPositionList.get(directionPositionList.size() - 1);

        int end_red = 100;
        int end_green = 100;
        int end_blue = 100;
        int red = 0;
        int green = 0;
        int blue = 0;

        end_red = getRed(Color.parseColor("#CC2F3E"));
        end_green = getGreen(Color.parseColor("#CC2F3E"));
        end_blue = getBlue(Color.parseColor("#CC2F3E"));

        red = getRed(Color.parseColor("#42B029"));
        green = getGreen(Color.parseColor("#42B029"));
        blue = getBlue(Color.parseColor("#42B029"));

        float redSteps;
        float greenSteps;
        float blueSteps;

        redSteps = ((float) (end_red - red) / 255) / (float) directionPositionList.size();
        greenSteps = ((float) (end_green - green) / 255) / (float) directionPositionList.size();
        blueSteps = ((float) (end_blue - blue) / 255) / (float) directionPositionList.size();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < directionPositionList.size() - 1; i++) {
            builder.include(directionPositionList.get(i));
            float redColor = ((float) red / 255) + (redSteps * i);
            float greenColor = ((float) green / 255) + (greenSteps * i);
            float blueColor = ((float) blue / 255) + (blueSteps * i);

            int color = getIntFromColor(redColor, greenColor, blueColor);
            gradientpoly.add(new PolylineOptions().width(8).color(color).geodesic(false).add(directionPositionList.get(i)).add(directionPositionList.get(i + 1)));
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            for (PolylineOptions po : gradientpoly) {
                line = mGoogleMap.addPolyline(po);
                line.setStartCap(new RoundCap());
                line.setEndCap(new RoundCap());
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirectionWayPoints(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        Route route = direction.getRouteList().get(0);
        int legCount = route.getLegList().size();

        for (int i = 0; i < legCount; i++) {
            Leg leg = route.getLegList().get(i);
            List<Step> stepList = leg.getStepList();
            List<PolylineOptions> polylineOptionList = new ArrayList<>();

            if (i == 0) {
                polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, ContextCompat.getColor(this, R.color.colorPrimary), 3, Color.BLUE);
            } else {
                polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            }

            for (PolylineOptions polylineOption : polylineOptionList) {
                if (mGoogleMap != null) {
                    mGoogleMap.addPolyline(polylineOption);
                }
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStopAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Polyline line;
    List<PolylineOptions> gradientpoly = new ArrayList<>();

    private void onDirectionWayPoints11(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            gradientpoly.clear();
        }

        Route route = direction.getRouteList().get(0);

        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        ArrayList<LatLng> directionPositionList1 = route.getLegList().get(1).getDirectionPoint();

        LatLng source = directionPositionList.get(0);
        LatLng addStop = directionPositionList.get(directionPositionList.size() - 1);
        LatLng dest = directionPositionList1.get(directionPositionList1.size() - 1);

        ArrayList<LatLng> directionPositionListFinal = new ArrayList<>();
        directionPositionListFinal.addAll(directionPositionList);
        directionPositionListFinal.addAll(directionPositionList1);

        int end_red = 100;
        int end_green = 100;
        int end_blue = 100;
        int red = 0;
        int green = 0;
        int blue = 0;

        end_red = getRed(Color.parseColor("#CC2F3E"));
        end_green = getGreen(Color.parseColor("#CC2F3E"));
        end_blue = getBlue(Color.parseColor("#CC2F3E"));

        red = getRed(Color.parseColor("#42B029"));
        green = getGreen(Color.parseColor("#42B029"));
        blue = getBlue(Color.parseColor("#42B029"));

        float redSteps;
        float greenSteps;
        float blueSteps;

        redSteps = ((float) (end_red - red) / 255) / (float) directionPositionListFinal.size();
        greenSteps = ((float) (end_green - green) / 255) / (float) directionPositionListFinal.size();
        blueSteps = ((float) (end_blue - blue) / 255) / (float) directionPositionListFinal.size();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < directionPositionListFinal.size() - 1; i++) {
            builder.include(directionPositionListFinal.get(i));
            float redColor = ((float) red / 255) + (redSteps * i);
            float greenColor = ((float) green / 255) + (greenSteps * i);
            float blueColor = ((float) blue / 255) + (blueSteps * i);

            int color = getIntFromColor(redColor, greenColor, blueColor);
            gradientpoly.add(new PolylineOptions().width(8).color(color).geodesic(false).add(directionPositionListFinal.get(i)).add(directionPositionListFinal.get(i + 1)));
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            for (PolylineOptions po : gradientpoly) {
                line = mGoogleMap.addPolyline(po);
                line.setStartCap(new RoundCap());
                line.setEndCap(new RoundCap());
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStop).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0x0ff;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0x0ff;
    }

    private int getBlue(int rgb) {
        return (rgb) & 0x0ff;
    }

    private int getIntFromColor(float Red, float Green, float Blue) {
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);
        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;
        return 0xFF000000 | R | G | B;
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.08);

        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void initializeMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            //  Objects.requireNonNull(mapFragment.getView()).setClickable(false);
            mapFragment.setRetainInstance(true);
            mapFragment.getMapAsync(TripInquiryActivity.this);
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.map_view, Objects.requireNonNull(mapFragment));
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap != null) {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            try {
                boolean success = mGoogleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.uber_style));
            } catch (Resources.NotFoundException e) {

            }
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }

    private void openDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.inquiry_bottom_sheet, null);

        LinearLayout select_from_gallery = dialogView.findViewById(R.id.select_from_gallery);
        LinearLayout click_camera = dialogView.findViewById(R.id.click_camera);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);

        select_from_gallery.setOnClickListener(v -> {
            dialog.cancel();
            isGallery = true;
            if (checkPermissions()) {
                ImagePicker.Companion.with(TripInquiryActivity.this).crop().galleryOnly().start();
            } else {
                requestPermissions();
            }
        });

        click_camera.setOnClickListener(v -> {
            dialog.cancel();
            isGallery = false;
            if (checkPermissions()) {
                ImagePicker.Companion.with(TripInquiryActivity.this).crop().cameraOnly().start();
            } else {
                requestPermissions();
            }
        });

        dialog.show();
    }

    private boolean checkPermissions() {
        int SD_CARD = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (SD_CARD != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        return listPermissionsNeeded.isEmpty();
    }

    private void requestPermissions() {

        boolean shouldProvideRationaleSD_CARD =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean shouldProvideRationaleCAMERA =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationaleSD_CARD || shouldProvideRationaleCAMERA) {
            showSnackbar(R.string.sdcard_permission,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(TripInquiryActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(TripInquiryActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (checkPermissions()) {
                    if (isGallery) {
                        ImagePicker.Companion.with(TripInquiryActivity.this).galleryOnly().start();
                    } else {
                        ImagePicker.Companion.with(TripInquiryActivity.this).cameraOnly().start();
                    }
                } else {
                    requestPermissions();
                }
            } else {
                // Permission denied.

                showSnackbar(R.string.loc_permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {

        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).setActionTextColor(Color.WHITE);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(14);
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // File object will not be null for RESULT_OK
            File file = ImagePicker.Companion.getFile(data);

            imagesList.add(file);
            inquiryImagesAdapter.addImages(imagesList);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Constant.showErrorToast(ImagePicker.Companion.getError(data), this);
        }
    }

    private void addFile() {

        inquiryImagesAdapter = new InquiryImagesAdapter(this);
        inquiryImagesAdapter.setInquiryImageDelete(this);
        activityTripInquiryBinding.inquiryImagesRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityTripInquiryBinding.inquiryImagesRecyclerview.setAdapter(inquiryImagesAdapter);
    }

    @Override
    public void deleteImage(File file) {
        imagesList.remove(file);
        inquiryImagesAdapter.addImages(imagesList);
    }
}
