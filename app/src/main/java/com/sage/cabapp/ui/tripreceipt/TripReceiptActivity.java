package com.sage.cabapp.ui.tripreceipt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityTripReceiptBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.tipthemmore.TipThemMoreActivity;
import com.sage.cabapp.ui.triphistory.model.TripHistoryResponseData;
import com.sage.cabapp.ui.tripinquirypage.TripInquiryActivity;
import com.sage.cabapp.ui.tripreceipt.adapter.HelpNeededAdapter;
import com.sage.cabapp.ui.tripreceipt.model.HelpNeededModel;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsData;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoyDetailsResponse;
import com.sage.cabapp.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripReceiptActivity extends BaseActivity<ActivityTripReceiptBinding, TripReceiptViewModel> implements TripReceiptNavigator, HelpClickCallback, OnMapReadyCallback {

    @Inject
    ViewModelProviderFactory factory;
    TripReceiptViewModel tripReceiptViewModel;
    ActivityTripReceiptBinding activityTripReceiptBinding;
    TripHistoryResponseData tripHistoryResponseData = null;

    private LatLng sourceAddressLatLng = null;
    private LatLng destinationAddressLatLng = null;
    private LatLng addStopAddressLatLng = null;
    private GoogleMap mGoogleMap;

    @Override
    public int getBindingVariable() {
        return BR.tripreceiptViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trip_receipt;
    }

    @Override
    public TripReceiptViewModel getViewModel() {
        tripReceiptViewModel = ViewModelProviders.of(this, factory).get(TripReceiptViewModel.class);
        return tripReceiptViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityTripReceiptBinding = getViewDataBinding();
        tripReceiptViewModel.setNavigator(this);

        activityTripReceiptBinding.helpRecycle.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TRIP_DETAIL")) {
            tripHistoryResponseData = (TripHistoryResponseData) intent.getSerializableExtra("TRIP_DETAIL");
        }

        initializeMap();

        if (tripHistoryResponseData != null) {

            activityTripReceiptBinding.tripDate.setText(Constant.convertDate(tripHistoryResponseData.getCreatedAt()));
            activityTripReceiptBinding.driverName.setText(tripHistoryResponseData.getDriverData().get(0).getFname());

            float rating = tripHistoryResponseData.getTripRating();

            activityTripReceiptBinding.rating.setRating(rating);

            Glide.with(this).load(tripHistoryResponseData.getDriverData().get(0).getProfile()).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityTripReceiptBinding.ivProfilePic);

        } else {
            vibrate();
            Constant.showErrorToast("No trip found", this);
            finish();
            slideLeftToRight();
        }

        setHelpData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {

            showLoading("");
            tripReceiptViewModel.tripHistoryDetailsWS(this, tripHistoryResponseData.getRequestId());

        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    private void setHelpData() {

        List<HelpNeededModel> helpNeededModelList = new ArrayList<>();

        helpNeededModelList.add(new HelpNeededModel("Report an issue or submit an inquiry on this trip", "issue"));
        helpNeededModelList.add(new HelpNeededModel("Submit 24/7 general inquiry", "24/7"));
        helpNeededModelList.add(new HelpNeededModel("See common topics section", "topic"));

        activityTripReceiptBinding.helpRecycle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        HelpNeededAdapter helpNeededAdapter = new HelpNeededAdapter(helpNeededModelList, this);
        helpNeededAdapter.setHelpClickCallback(this);
        activityTripReceiptBinding.helpRecycle.setAdapter(helpNeededAdapter);
        helpNeededAdapter.notifyDataSetChanged();
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
    public void tipThemMore() {

        Intent intent = new Intent(this, TipThemMoreActivity.class);
        intent.putExtra("TRIP_DETAIL", tripHistoryResponseData);
        startActivity(intent);
        slideRightToLeft();
    }

    @Override
    public void successAPI(TripHistoyDetailsResponse response) {

        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData() != null) {

                    TripHistoryDetailsData tripHistoryDetailsData = response.getData();

                    activityTripReceiptBinding.sourceAddress.setText(tripHistoryDetailsData.getSAddress());
                    activityTripReceiptBinding.destinationAddress.setText(tripHistoryDetailsData.getDAddress());

                    activityTripReceiptBinding.startTime.setText(Constant.convertTime(tripHistoryDetailsData.getStartimeFirst()));
                    activityTripReceiptBinding.endTime.setText(Constant.convertTime(tripHistoryDetailsData.getEndtimeSecond()));

                    activityTripReceiptBinding.minimumFare.setText(String.format("$%s", tripHistoryDetailsData.getMinimumFare()));
                    activityTripReceiptBinding.thirdPartyFee.setText(String.format("$%s", tripHistoryDetailsData.getThridParty()));

                    activityTripReceiptBinding.perMinuteText.setText(tripHistoryDetailsData.getRideTime());
                    activityTripReceiptBinding.perMinute.setText(String.format("$%s", tripHistoryDetailsData.getRideTimeMoney()));

                    activityTripReceiptBinding.perMileText.setText(String.format("%s mile", tripHistoryDetailsData.getRideDistance()));
                    activityTripReceiptBinding.perMile.setText(String.format("$%s", tripHistoryDetailsData.getRideDistanceMoney()));

                    activityTripReceiptBinding.totalTripAmount.setText(String.format("$%s", tripHistoryDetailsData.getTripPayment()));
                    activityTripReceiptBinding.totalTipAmount.setText(String.format("+ $%s", tripHistoryDetailsData.getTripTip()));

                    sourceAddressLatLng = new LatLng(Double.parseDouble(tripHistoryDetailsData.getsLat()), Double.parseDouble(tripHistoryDetailsData.getsLng()));
                    destinationAddressLatLng = new LatLng(Double.parseDouble(tripHistoryDetailsData.getDLat()), Double.parseDouble(tripHistoryDetailsData.getDLng()));
                    addStopAddressLatLng = new LatLng(Double.parseDouble(tripHistoryDetailsData.getExLat()), Double.parseDouble(tripHistoryDetailsData.getExLng()));

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

    @Override
    public void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    public void openHelp(HelpNeededModel helpNeededModel) {

        Intent intent = new Intent(this, TripInquiryActivity.class);
        intent.putExtra("TRIP_DETAIL", tripHistoryResponseData);
        intent.putExtra("sourceAddressLatLng", sourceAddressLatLng);
        intent.putExtra("destinationAddressLatLng", destinationAddressLatLng);
        if (addStopAddressLatLng != null){
            intent.putExtra("addStopAddressLatLng", addStopAddressLatLng);
        }
        startActivity(intent);
        slideRightToLeft();

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
            mapFragment.getMapAsync(TripReceiptActivity.this);
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
}
